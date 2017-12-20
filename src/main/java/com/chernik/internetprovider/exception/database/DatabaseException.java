package com.chernik.internetprovider.exception.database;

import com.chernik.internetprovider.exception.InternetProviderException;

public class DatabaseException extends InternetProviderException {
    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
