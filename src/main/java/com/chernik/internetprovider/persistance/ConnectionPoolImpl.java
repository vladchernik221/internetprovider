package com.chernik.internetprovider.persistance;

import com.chernik.internetprovider.exception.database.DatabaseException;
import com.chernik.internetprovider.exception.database.TimeOutException;
import com.chernik.internetprovider.property.PropertyHolder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool, Runnable {
    private final static Logger LOGGER = LogManager.getLogger(ConnectionPoolImpl.class);

    private final static String DATABASE_CONNECTION_STRING = "%s?user=%s&password=%s&" +
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
    private int connectionCount;


    private ConnectionPoolImpl() {
        initDatabaseProperty();
        LOGGER.log(Level.INFO, "Connection pool was initialized");
    }

    private void initDatabaseProperty() {
        String driver = getPropertyWithValidate(DRIVER_PROPERTY_NAME);
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.log(Level.FATAL, "Can't find database driver {}", driver);
            throw new RuntimeException(String.format("Can't find database driver %s", driver), e);
        }
        url = getPropertyWithValidate(URL_PROPERTY_NAME);
        user = getPropertyWithValidate(USER_PROPERTY_NAME);
        password = getPropertyWithValidate(PASSWORD_PROPERTY_NAME);

        String maxConnectionsProperty = getProperty(MAX_CONNECTIONS_PROPERTY_NAME);
        if (maxConnectionsProperty != null && Integer.parseInt(maxConnectionsProperty) > 0) {
            maxConnections = Integer.parseInt(maxConnectionsProperty);
        }
        String timeOutProperty = getProperty(TIMEOUT_PROPERTY_NAME);
        if (timeOutProperty != null && Integer.parseInt(timeOutProperty) > 0) {
            timeOut = Integer.parseInt(timeOutProperty);
        }
    }

    private String getPropertyWithValidate(String propertyName) {
        String property = getProperty(propertyName);
        if (property == null) {
            throw new RuntimeException(String.format("Mandatory property %s was not found", propertyName));
        }
        return property;
    }

    private String getProperty(String propertyName) {
        PropertyHolder propertyHolder = new PropertyHolder();
        return propertyHolder.getProperty(propertyName);
    }

    public static ConnectionPoolImpl getInstance() {
        LOGGER.log(Level.TRACE, "Getting connection pool from thread {}", Thread.currentThread().getName());
        return ConnectionPoolSingletonHolder.INSTANCE;
    }


    @Override
    public Connection getConnection() throws DatabaseException, TimeOutException {
        try {
            LOGGER.log(Level.TRACE, "Available connections {}", availableConnections.size());
            locker.lock();
            LOGGER.log(Level.TRACE, "All open connections {}", connectionCount);
            if (!availableConnections.isEmpty()) {
                return getWhenAvailableConnectionsIsNotEmpty();
            } else if (connectionCount < maxConnections) {
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
            return getConnection();
        } else {
            busyConnections.push(connection);
            LOGGER.log(Level.DEBUG, "Get connection. Available connections count: {}", availableConnections.size());
            return connection;
        }
    }

    private void getWhenAvailableConnectionsIsEmpty() {
        connectionCount++;
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

    private void waitForConnectionCreate() throws DatabaseException {
        LOGGER.log(Level.TRACE, "Waiting for connection creating");
        try {
            creationLocker.lock();
            creationCondition.await();
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
        locker.lock();
        closeConnections(availableConnections);
        availableConnections = new ArrayDeque<>();
        closeConnections(busyConnections);
        busyConnections = new ArrayDeque<>();
        locker.unlock();
        LOGGER.log(Level.INFO, "All connection was closed");
    }

    private void closeConnections(ArrayDeque<Connection> connections) {
        connections.forEach(connection -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARN, "Can't close connection {}", connection);
            }
        });
    }

    private Connection createConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(String.format(DATABASE_CONNECTION_STRING, url, user, password));
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, "Can't connect to database. Url: {}, user: {}", url, user);
            throw new RuntimeException(String.format("Can't connect to database. Url: %s, user: %s", url, user), e);
        }
        LOGGER.log(Level.TRACE, "New connection was created.");
        return connection;
    }

    @Override
    public void run() {
        creationLocker.lock();
        Connection connection = createConnection();
        availableConnections.add(connection);
        creationCondition.signalAll();
        creationLocker.unlock();
        LOGGER.log(Level.DEBUG, "New connection is available. Available connections count: {}", availableConnections.size());
    }


    private static class ConnectionPoolSingletonHolder {
        private final static ConnectionPoolImpl INSTANCE = new ConnectionPoolImpl();
    }
}
