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
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class TransactionalConnectionPool implements ConnectionPool, TransactionManager {
    private static final Logger LOGGER = LogManager.getLogger(TransactionalConnectionPool.class);

    private ConnectionPoolImpl connectionPool;
    private Map<Thread, Entry<Connection, Integer>> connectionCache = Collections.synchronizedMap(new HashMap<>());

    public TransactionalConnectionPool() {
        connectionPool = ConnectionPoolImpl.getInstance();
    }

    @BeforeDestroy
    public void destroyConnectionPool() {
        connectionPool.closeAllConnections();
    }

    @Override
    public Connection getConnection() throws DatabaseException, TimeOutException {
        Entry<Connection, Integer> connectionEntry = connectionCache.get(Thread.currentThread());
        if (connectionEntry != null) {
            return connectionEntry.getKey();
        } else {
            return connectionPool.getConnection();
        }
    }

    @Override
    public void releaseConnection(Connection connection) throws DatabaseException {
        if (!connectionCache.containsKey(Thread.currentThread())) {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void openTransaction() throws DatabaseException, TimeOutException, SQLException {
        Entry<Connection, Integer> connectionEntry = connectionCache.get(Thread.currentThread());
        if (connectionEntry == null) {
            LOGGER.log(Level.DEBUG, "Open transaction");
            Connection connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            connectionCache.put(Thread.currentThread(), new SimpleEntry<>(connection, 1));
        } else {
            Integer counter = connectionEntry.getValue();
            counter = counter + 1;
            connectionEntry.setValue(counter);
        }
    }

    @Override
    public void commit() throws SQLException, DatabaseException {
        Entry<Connection, Integer> connectionEntry = connectionCache.get(Thread.currentThread());
        Connection connection = connectionEntry.getKey();
        Integer counter = connectionEntry.getValue();

        if (counter == 1) {
            LOGGER.log(Level.DEBUG, "Commit transaction");
            connectionCache.remove(Thread.currentThread());
            connection.commit();
            connection.setAutoCommit(true);
            connectionPool.releaseConnection(connection);
        } else {
            counter = counter - 1;
            connectionEntry.setValue(counter);
        }
    }

    @Override
    public void rollback() throws SQLException, DatabaseException {
        Entry<Connection, Integer> connectionEntry = connectionCache.get(Thread.currentThread());
        Connection connection = connectionEntry.getKey();
        Integer counter = connectionEntry.getValue();
        if (counter == 1) {
            LOGGER.log(Level.DEBUG, "Rollback transaction");
            connectionCache.remove(Thread.currentThread());
            connection.rollback();
            connection.setAutoCommit(true);
            connectionPool.releaseConnection(connection);
        } else {
            counter = counter - 1;
            connectionEntry.setValue(counter);
        }
    }
}
