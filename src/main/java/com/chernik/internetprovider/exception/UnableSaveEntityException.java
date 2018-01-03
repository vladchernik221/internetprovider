package com.chernik.internetprovider.exception;

import javax.servlet.http.HttpServletResponse;

public class UnableSaveEntityException extends BaseException {
    public UnableSaveEntityException() {
    }

    public UnableSaveEntityException(String message) {
        super(message);
    }

    public UnableSaveEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableSaveEntityException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getStatusCode() {
        return HttpServletResponse.SC_CONFLICT;
    }
}
