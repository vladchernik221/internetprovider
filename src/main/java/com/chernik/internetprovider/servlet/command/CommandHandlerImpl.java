package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandHandlerImpl implements CommandHandler {
    private final static Logger LOGGER = LogManager.getLogger(CommandHandlerImpl.class);

    private Map<HttpRequestParameter, Command> commands = new HashMap<>();


    @AfterCreate
    public void registerCommands(List<Command> commandList) {
        commandList.forEach(command -> {
            HttpRequestProcessor processor = command.getClass().getAnnotation(HttpRequestProcessor.class);
            commands.put(new HttpRequestParameter(processor.uri().toLowerCase(), processor.method()), command);
            LOGGER.log(Level.DEBUG, "Mapped {{}, {}} onto {}", processor.uri(), processor.method(), command.getClass());
        });
    }

    @Override
    public Command getCommand(HttpRequestParameter parameter) throws EntityNotFoundException {
        Command command = commands.get(parameter);
        if (command == null) {
            throw new EntityNotFoundException();//TODO entity
        }
        return command;
    }
}
