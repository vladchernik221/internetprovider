package com.chernik.internetprovider.context;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;

import java.sql.SQLException;

public interface TransactionManager {
    void openTransaction() throws DatabaseException, TimeOutException, SQLException;

    void commit() throws SQLException, DatabaseException;

    void rollback() throws SQLException, DatabaseException;
}
