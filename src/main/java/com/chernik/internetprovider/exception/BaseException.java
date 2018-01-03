package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class BaseException extends Exception {
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public int getStatusCode() {
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
