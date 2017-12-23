package com.chernik.internetprovider.exception;

public class FrontControllerException extends InternetProviderException {
    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public FrontControllerException(String statusCode) {
        this.statusCode = statusCode;
    }

    public FrontControllerException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public FrontControllerException(String message, Throwable cause, String statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public FrontControllerException(Throwable cause, String statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }
}
