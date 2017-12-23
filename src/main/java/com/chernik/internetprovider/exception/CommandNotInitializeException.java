package com.chernik.internetprovider.exception;

public class CommandNotInitializeException extends InternetProviderException {
    public CommandNotInitializeException() {
    }

    public CommandNotInitializeException(String message) {
        super(message);
    }

    public CommandNotInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotInitializeException(Throwable cause) {
        super(cause);
    }
}
