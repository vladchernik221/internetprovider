package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.exception.CommandNotFoundException;

public interface CommandHandler {
    Command getCommand(HttpRequestParameter parameter) throws CommandNotFoundException;
}
