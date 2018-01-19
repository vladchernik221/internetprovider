package com.chernik.internetprovider.persistence;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection() throws DatabaseException, TimeOutException;

    void releaseConnection(Connection connection) throws DatabaseException;
}
