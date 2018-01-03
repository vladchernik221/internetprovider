package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.exception.EntityNotFoundException;

public interface CommandHandler {
    Command getCommand(HttpRequestParameter parameter) throws EntityNotFoundException;
}
