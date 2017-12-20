package com.chernik.internetprovider.exception;

public class InternetProviderException extends Exception {
    public InternetProviderException() {
    }

    public InternetProviderException(String message) {
        super(message);
    }

    public InternetProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternetProviderException(Throwable cause) {
        super(cause);
    }
}
