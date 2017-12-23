package com.chernik.internetprovider.persistence;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.property.PropertyHolder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool, Runnable {//TODO something with closing
    private final static Logger LOGGER = LogManager.getLogger(ConnectionPoolImpl.class);

    private final static String DATABASE_CONNECTION_FORMAT = "%s?user=%s&password=%s&" +
            "verifyServerCertificate=false&useSSL=true&serverTimezone=UTC";
    private final static String DRIVER_PROPERTY_NAME = "database.driver";
    private final static String URL_PROPERTY_NAME = "database.url";
    private final static String USER_PROPERTY_NAME = "database.user";
    private final static String PASSWORD_PROPERTY_NAME = "database.password";
    private final static String MAX_CONNECTIONS_PROPERTY_NAME = "database.connections.count";
    private final static String TIMEOUT_PROPERTY_NAME = "database.connections.timeOut";

    private Lock locker = new ReentrantLock();
    private Condition condition = locker.newCondition();
    private Lock creationLocker = new ReentrantLock();
    private Condition creationCondition = creationLocker.newCondition();

    private String url;
    private String user;
    private String password;
    private int maxConnections = 20;
    private int timeOut = 5;

    private ArrayDeque<Connection> availableConnections = new ArrayDeque<>();
    private ArrayDeque<Connection> busyConnections = new ArrayDeque<>();
    private AtomicInteger connectionCount = new AtomicInteger();


    public ConnectionPoolImpl() {
        initDatabaseProperty();
        LOGGER.log(Level.INFO, "Connection pool was initialized");
    }

    private void initDatabaseProperty() {
        String driver = readPropertyWithValidation(DRIVER_PROPERTY_NAME);
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.log(Level.FATAL, "Can't find database driver {}", driver);
            throw new RuntimeException(String.format("Can't find database driver %s", driver), e);
        }
        url = readPropertyWithValidation(URL_PROPERTY_NAME);
        user = readPropertyWithValidation(USER_PROPERTY_NAME);
        password = readPropertyWithValidation(PASSWORD_PROPERTY_NAME);

        String maxConnectionsProperty = readProperty(MAX_CONNECTIONS_PROPERTY_NAME);
        if (maxConnectionsProperty != null && Integer.parseInt(maxConnectionsProperty) > 0) {
            maxConnections = Integer.parseInt(maxConnectionsProperty);
        }
        String timeOutProperty = readProperty(TIMEOUT_PROPERTY_NAME);
        if (timeOutProperty != null && Integer.parseInt(timeOutProperty) > 0) {
            timeOut = Integer.parseInt(timeOutProperty);
        }
    }

    private String readPropertyWithValidation(String propertyName) {
        String property = readProperty(propertyName);
        if (property == null) {
            throw new RuntimeException(String.format("Mandatory property %s was not found", propertyName));
        }
        return property;
    }

    private String readProperty(String propertyName) {
        PropertyHolder propertyHolder = new PropertyHolder();
        return propertyHolder.getProperty(propertyName);
    }


    @Override
    public Connection getConnection() throws DatabaseException, TimeOutException {
        try {
            LOGGER.log(Level.TRACE, "Available connections {}", availableConnections.size());
            locker.lock();
            LOGGER.log(Level.TRACE, "All open connections {}", connectionCount);
            if (!availableConnections.isEmpty()) {
                return getWhenAvailableConnectionsIsNotEmpty();
            } else if (connectionCount.get() < maxConnections) {
                getWhenAvailableConnectionsIsEmpty();
            } else {
                return getAfterFreed();
            }
        } catch (InterruptedException e) {
            throw new DatabaseException(String.format("Thread %s was interrupted", Thread.currentThread().getName()));
        } catch (SQLException e) {
            throw new DatabaseException("Database access error", e);
        } finally {
            locker.unlock();
        }

        waitForConnectionCreate();

        return getConnection();
    }

    private Connection getWhenAvailableConnectionsIsNotEmpty() throws SQLException, DatabaseException, TimeOutException {
        Connection connection;
        connection = availableConnections.pop();
        if (connection.isClosed()) {
            connectionCount.decrementAndGet();
            return getConnection();
        } else {
            busyConnections.push(connection);
            LOGGER.log(Level.DEBUG, "Get connection. Available connections count: {}", availableConnections.size());
            return connection;
        }
    }

    private void getWhenAvailableConnectionsIsEmpty() {
        connectionCount.incrementAndGet();
        makeBackGroundConnection();
    }

    private Connection getAfterFreed() throws TimeOutException, InterruptedException {
        waitForConnectionFound();
        LOGGER.log(Level.DEBUG, "Get connection. Available connections count: {}", availableConnections.size());
        Connection connection = availableConnections.pop();
        busyConnections.add(connection);
        return connection;
    }

    private void waitForConnectionFound() throws InterruptedException, TimeOutException {
        while (availableConnections.isEmpty()) {
            if (!condition.await(timeOut, TimeUnit.SECONDS)) {
                throw new TimeOutException(String.format("Time out in thread: %s", Thread.currentThread().getName()));
            }
        }
    }

    private void waitForConnectionCreate() throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Waiting for connection creating");
        try {
            creationLocker.lock();
            if (!creationCondition.await(timeOut, TimeUnit.SECONDS)) {
                throw new TimeOutException(String.format("Time out in thread: %s", Thread.currentThread().getName()));
            }
        } catch (InterruptedException e) {
            throw new DatabaseException(String.format("Thread %s was interrupted", Thread.currentThread().getName()));
        } finally {
            creationLocker.unlock();
        }
    }

    private void makeBackGroundConnection() {
        new Thread(this).start();
    }

    @Override
    public void releaseConnection(Connection connection) throws DatabaseException {
        try {
            locker.lock();
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

    @Override
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
        LOGGER.log(Level.INFO, "All connection was closed");
    }

    private void closeConnections(ArrayDeque<Connection> connections) {
        connections.forEach(connection -> {
            try {
                if (!connection.isClosed()) {
                    ConnectionWrapper connectionWrapper = (ConnectionWrapper) connection;
                    connectionWrapper.realClose();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARN, "Can't close connection {}", connection);
            }
        });
    }

    private Connection createConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(String.format(DATABASE_CONNECTION_FORMAT, url, user, password));
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, "Can't connect to database. Url: {}, user: {}", url, user);
            throw new RuntimeException(String.format("Can't connect to database. Url: %s, user: %s", url, user), e);
        }
        LOGGER.log(Level.TRACE, "New connection was created.");
        return new ConnectionWrapper(connection);
    }

    @Override
    public void run() {
        try {
            Connection connection = createConnection();
            creationLocker.lock();
            availableConnections.add(connection);
        } finally {
            creationCondition.signalAll();
            creationLocker.unlock();
        }
        LOGGER.log(Level.DEBUG, "New connection is available. Available connections count: {}", availableConnections.size());
    }


    private class ConnectionWrapper implements Connection {
        private Connection connection;

        ConnectionWrapper(Connection connection) {
            this.connection = connection;
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

        }

        void realClose() throws SQLException {
            connection.close();
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
