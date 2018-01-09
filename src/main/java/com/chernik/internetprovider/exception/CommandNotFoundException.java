package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class CommandNotFoundException extends BaseException {
    public CommandNotFoundException() {
    }

    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getStatusCode() {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
