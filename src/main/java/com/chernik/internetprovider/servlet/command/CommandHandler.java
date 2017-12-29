package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.exception.FrontControllerException;

public interface CommandHandler {
    Command getCommand(HttpRequestParameter parameter) throws FrontControllerException;
}
