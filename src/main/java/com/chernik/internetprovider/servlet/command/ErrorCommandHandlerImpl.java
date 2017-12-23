package com.chernik.internetprovider.servlet.command;

import java.util.HashMap;
import java.util.Map;

public class ErrorCommandHandlerImpl implements CommandHandler {
    private Map<HttpRequestParameter, Command> commands = new HashMap<>();

    @Override
    public void registerCommand(HttpRequestParameter parameter, Command command) {
        commands.put(parameter, command);
    }

    @Override
    public Command getCommand(HttpRequestParameter parameter) {
        return commands.get(parameter);
    }
}
