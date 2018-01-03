package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getStatusCode() {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
