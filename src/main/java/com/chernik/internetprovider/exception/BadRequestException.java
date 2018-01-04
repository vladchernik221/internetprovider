package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class BadRequestException extends BaseException {
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getStatusCode() {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
