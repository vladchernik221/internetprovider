package com.chernik.internetprovider.persistance;

import com.chernik.internetprovider.exception.database.DatabaseException;
import com.chernik.internetprovider.exception.database.TimeOutException;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection() throws DatabaseException, TimeOutException;

    void releaseConnection(Connection connection) throws DatabaseException;

    void closeAllConnections();
}
