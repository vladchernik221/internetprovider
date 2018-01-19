package com.chernik.internetprovider.exception;

public class ContextInitializationException extends BaseException {
    public ContextInitializationException() {
    }

    public ContextInitializationException(String message) {
        super(message);
    }

    public ContextInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextInitializationException(Throwable cause) {
        super(cause);
    }
}
