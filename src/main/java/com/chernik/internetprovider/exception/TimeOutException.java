package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class TimeOutException extends BaseException {
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

    @Override
    public int getStatusCode() {
        return HttpServletResponse.SC_REQUEST_TIMEOUT;
    }
}
