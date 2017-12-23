package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.exception.FrontControllerException;

public interface CommandHandler {
    void registerCommand(HttpRequestParameter parameters, Command command);

    Command getCommand(HttpRequestParameter parameters) throws FrontControllerException;
}
