package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.exception.FrontControllerException;

import java.util.HashMap;
import java.util.Map;

public class FrontCommandHandlerImpl implements CommandHandler {
    private Map<HttpRequestParameter, Command> commands = new HashMap<>();

    @Override
    public void registerCommand(HttpRequestParameter parameter, Command command) {
        commands.put(parameter, command);
    }

    @Override
    public Command getCommand(HttpRequestParameter parameter) throws FrontControllerException {
        Command command = commands.get(parameter);
        if(command == null){
            throw new FrontControllerException("404");
        }
        return command;
    }
}
