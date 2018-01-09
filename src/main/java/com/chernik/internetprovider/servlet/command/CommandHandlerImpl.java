package com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.CommandNotFoundException;
import com.chernik.internetprovider.service.RegularExpressionService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandHandlerImpl implements CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(CommandHandlerImpl.class);

    private static final String DYNAMIC_URI_REGULAR_EXPRESSION = ".*\\{.+}.*";

    private Map<RequestParameter, Command> commands = new HashMap<>();
    private RegexHandler dynamicCommands = new RegexHandler();

    @Autowired
    private RegularExpressionService regularExpressionService;

    @AfterCreate
    public void registerCommands(List<Command> commandList) {
        commandList.forEach(command -> {
            HttpRequestProcessor processor = command.getClass().getAnnotation(HttpRequestProcessor.class);
            String uri = processor.uri().toLowerCase();
            commands.put(new RequestParameter(uri, processor.method()), command);
            if (regularExpressionService.checkToRegularExpression(uri, DYNAMIC_URI_REGULAR_EXPRESSION)) {
                dynamicCommands.put(uri);
            }
            LOGGER.log(Level.DEBUG, "Mapped {{}, {}} onto {}", processor.uri(), processor.method(), command.getClass());
        });
    }

    @Override
    public Command getCommand(RequestParameter parameter) throws CommandNotFoundException {
        Command command = commands.get(parameter);
        if (command == null) {
            String dynamicUri = dynamicCommands.get(parameter.getUri());
            if (dynamicUri != null) {
                RequestParameter dynamicParameter = new RequestParameter(dynamicUri, parameter.getType());
                command = commands.get(dynamicParameter); //TODO can be null when URI exist but Type is different
            } else {
                throw new CommandNotFoundException(String.format("Request: %s, method: %s does not support", parameter.getUri(), parameter.getType()));
            }
        }
        return command;
    }
}
