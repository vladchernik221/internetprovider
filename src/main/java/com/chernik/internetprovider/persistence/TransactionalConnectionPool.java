package com.chernik.internetprovider.persistence;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.BeforeDestroy;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionalConnectionPool implements ConnectionPool, TransactionManager {
    private ConnectionPoolImpl connectionPool;
    private Map<Thread, Connection> connectionCache = new HashMap<>();

    public TransactionalConnectionPool() {
        connectionPool = new ConnectionPoolImpl();
    }

    @AfterCreate
    public void initConnectionPool() {
        connectionPool.initDatabaseProperty();
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
        connection.setAutoCommit(false);
        connectionCache.put(Thread.currentThread(), connection);
    }

    @Override
    public void commit() throws SQLException, DatabaseException {
        Connection connection = connectionCache.get(Thread.currentThread());
        connection.commit();
        connection.setAutoCommit(true);
        connectionPool.releaseConnection(connection);
    }

    @Override
    public void rollback() throws SQLException, DatabaseException {
        Connection connection = connectionCache.get(Thread.currentThread());
        connection.rollback();
        connection.setAutoCommit(true);
        connectionPool.releaseConnection(connection);
    }
}
