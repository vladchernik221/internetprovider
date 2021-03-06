package com.chernik.internetprovider.persistence;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolImpl.class);

    private static final String PROPERTY_FILE_NAME = "application";
    private static final String DATABASE_CONNECTION_FORMAT = "%s&user=%s&password=%s";
    private static final String DRIVER_PROPERTY_NAME = "database.driver";
    private static final String URL_PROPERTY_NAME = "database.url";
    private static final String USER_PROPERTY_NAME = "database.user";
    private static final String PASSWORD_PROPERTY_NAME = "database.password";
    private static final String MAX_CONNECTIONS_PROPERTY_NAME = "database.connections.count";
    private static final String TIMEOUT_PROPERTY_NAME = "database.connections.timeOut";

    private Lock locker = new ReentrantLock();
    private Condition condition = locker.newCondition();

    private String url;
    private String user;
    private String password;
    private int maxConnections = 20;
    private int timeOut = 5;

    private Deque<Connection> availableConnections = new ArrayDeque<>();
    private Deque<Connection> busyConnections = new ArrayDeque<>();
    private AtomicInteger connectionCount = new AtomicInteger();

    private ConnectionPoolImpl() {
        initDatabaseProperty();
    }

    public static ConnectionPoolImpl getInstance() {
        return ConnectionPoolImplSingletonHolder.INSTANCE;
    }

    private void initDatabaseProperty() {
        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTY_FILE_NAME);

        String driver = readPropertyWithValidation(bundle, DRIVER_PROPERTY_NAME);
        try {
            TimeZone timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName());
            TimeZone.setDefault(timeZone);
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String message = String.format("Can't find database driver %s", driver);
            LOGGER.log(Level.FATAL, message, e);
            throw new RuntimeException(message, e);
        }
        url = readPropertyWithValidation(bundle, URL_PROPERTY_NAME);
        user = readPropertyWithValidation(bundle, USER_PROPERTY_NAME);
        password = readPropertyWithValidation(bundle, PASSWORD_PROPERTY_NAME);

        String maxConnectionsProperty = bundle.getString(MAX_CONNECTIONS_PROPERTY_NAME);
        if (!maxConnectionsProperty.isEmpty() && Integer.parseInt(maxConnectionsProperty) > 0) {
            maxConnections = Integer.parseInt(maxConnectionsProperty);
        }
        String timeOutProperty = bundle.getString(TIMEOUT_PROPERTY_NAME);
        if (!timeOutProperty.isEmpty() && Integer.parseInt(timeOutProperty) > 0) {
            timeOut = Integer.parseInt(timeOutProperty);
        }
    }

    private String readPropertyWithValidation(ResourceBundle bundle, String propertyName) {
        String property = bundle.getString(propertyName);
        if (property.isEmpty()) {
            String message = String.format("Mandatory property %s was not found", propertyName);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message);
        }
        return property;
    }


    @Override
    public Connection getConnection() throws DatabaseException, TimeOutException {
        Connection connection;

        try {
            LOGGER.log(Level.TRACE, "Available connections {}. All open connections {}",
                    availableConnections.size(), connectionCount);
            locker.lock();
            if (!availableConnections.isEmpty()) {
                connection = getAvailableConnection();
            } else if (connectionCount.get() < maxConnections) {
                connection = getNewConnection();
            } else {
                connection = getAfterFreed();
            }
        } catch (InterruptedException e) {
            throw new DatabaseException(String.format("Thread %s was interrupted", Thread.currentThread().getName()), e);
        } catch (SQLException e) {
            throw new DatabaseException("Database access error", e);
        } finally {
            locker.unlock();
        }

        busyConnections.push(connection);
        return new ConnectionWrapper(connection);
    }

    private Connection getAvailableConnection() throws SQLException, DatabaseException, TimeOutException {
        Connection connection;
        connection = availableConnections.pop();
        if (connection.isClosed()) {
            connectionCount.decrementAndGet();
            return getConnection();
        } else {
            LOGGER.log(Level.DEBUG, "Get connection. Available connections count: {}", availableConnections.size());
            return connection;
        }
    }

    private Connection getNewConnection() {
        Connection connection = createConnection();
        connectionCount.incrementAndGet();
        LOGGER.log(Level.DEBUG, "New connection is available. Available connections count: {}", availableConnections.size());
        return connection;
    }

    private Connection getAfterFreed() throws TimeOutException, InterruptedException {
        while (availableConnections.isEmpty()) {
            if (!condition.await(timeOut, TimeUnit.SECONDS)) {
                throw new TimeOutException(String.format("Time out in thread: %s", Thread.currentThread().getName()));
            }
        }
        LOGGER.log(Level.DEBUG, "Get connection. Available connections count: {}", availableConnections.size());
        return availableConnections.pop();
    }

    @Override
    public void releaseConnection(Connection connection) throws DatabaseException {
        try {
            locker.lock();
            if (connection.getClass() != ConnectionWrapper.class) {
                throw new DatabaseException(String.format("Connection %s does not in the pool", connection));
            }
            connection = ((ConnectionWrapper) connection).getConnection();
            if (!busyConnections.remove(connection)) {
                throw new DatabaseException(String.format("Connection %s does not in the pool", connection));
            }

            availableConnections.push(connection);
            condition.signalAll();
        } finally {
            locker.unlock();
        }
        LOGGER.log(Level.DEBUG, "Release connection. Available connections count: {}", availableConnections.size());
    }

    public void closeAllConnections() {
        try {
            locker.lock();
            closeConnections(availableConnections);
            availableConnections = new ArrayDeque<>();
            closeConnections(busyConnections);
            busyConnections = new ArrayDeque<>();
            connectionCount = new AtomicInteger();
        } finally {
            locker.unlock();
        }
        LOGGER.log(Level.INFO, "All connections were closed");
    }

    private void closeConnections(Deque<Connection> connections) {
        connections.forEach(connection -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARN, "Can't close connection", e);
            }
        });
    }

    private Connection createConnection() {
        Connection connection;
        try {
            LOGGER.log(Level.INFO, "Connect to database. Url: {}, user: {}", url, user);
            connection = DriverManager.getConnection(String.format(DATABASE_CONNECTION_FORMAT, url, user, password));
        } catch (SQLException e) {
            String message = String.format("Can't connect to database. Url: %s, user: %s", url, user);
            LOGGER.log(Level.FATAL, message, e);
            throw new RuntimeException(message, e);
        }
        LOGGER.log(Level.TRACE, "New connection was created");
        return connection;
    }


    private static class ConnectionPoolImplSingletonHolder {
        private static final ConnectionPoolImpl INSTANCE = new ConnectionPoolImpl();
    }

    private class ConnectionWrapper implements Connection {
        private Connection connection;

        ConnectionWrapper(Connection connection) {
            this.connection = connection;
        }

        Connection getConnection() {
            return connection;
        }

        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return connection.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {
            connection.commit();
        }

        @Override
        public void rollback() throws SQLException {
            connection.rollback();
        }

        @Override
        public void close() {
            String message = "Connection closing is unsupported operation";
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message);
        }

        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            connection.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            connection.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            connection.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            connection.setClientInfo(properties);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            connection.setSchema(schema);
        }

        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            connection.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            connection.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }
    }
}
