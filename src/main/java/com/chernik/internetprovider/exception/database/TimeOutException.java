package com.chernik.internetprovider.exception.database;

import com.chernik.internetprovider.exception.InternetProviderException;

public class TimeOutException extends InternetProviderException  {
    public TimeOutException() {
    }

    public TimeOutException(String message) {
        super(message);
    }

    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOutException(Throwable cause) {
        super(cause);
    }
}
