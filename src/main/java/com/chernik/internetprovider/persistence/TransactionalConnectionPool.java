package com.chernik.internetprovider.persistence;

import com.chernik.internetprovider.context.BeforeDestroy;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.context.TransactionManager;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionalConnectionPool implements ConnectionPool, TransactionManager {
    private static final Logger LOGGER = LogManager.getLogger(TransactionalConnectionPool.class);

    private ConnectionPoolImpl connectionPool;
    private Map<Thread, Connection> connectionCache = Collections.synchronizedMap(new HashMap<>());

    public TransactionalConnectionPool() {
        connectionPool = ConnectionPoolImpl.getInstance();
    }

    @BeforeDestroy
    public void destroyConnectionPool() {
        connectionPool.closeAllConnections();
    }

    @Override
    public Connection getConnection() throws DatabaseException, TimeOutException {
        Connection connection = connectionCache.get(Thread.currentThread());
        if (connection != null) {
            return connection;
        } else {
            return connectionPool.getConnection();
        }
    }

    @Override
    public void releaseConnection(Connection connection) throws DatabaseException {
        Connection transactionConnection = connectionCache.get(Thread.currentThread());
        if (transactionConnection == null) {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void openTransaction() throws DatabaseException, TimeOutException, SQLException {
        Connection connection = connectionPool.getConnection();
        LOGGER.log(Level.DEBUG, "Open transaction");
        connection.setAutoCommit(false);
        connectionCache.put(Thread.currentThread(), connection);
    }

    @Override
    public void commit() throws SQLException, DatabaseException {
        Connection connection = connectionCache.remove(Thread.currentThread());

        LOGGER.log(Level.DEBUG, "Commit transaction");
        connection.commit();
        connection.setAutoCommit(true);
        connectionPool.releaseConnection(connection);
    }

    @Override
    public void rollback() throws SQLException, DatabaseException {
        Connection connection = connectionCache.remove(Thread.currentThread());

        LOGGER.log(Level.DEBUG, "Rollback transaction");
        connection.rollback();
        connection.setAutoCommit(true);
        connectionPool.releaseConnection(connection);
    }
}
