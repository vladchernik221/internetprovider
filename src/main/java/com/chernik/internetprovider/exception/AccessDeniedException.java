package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getStatusCode() {
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
