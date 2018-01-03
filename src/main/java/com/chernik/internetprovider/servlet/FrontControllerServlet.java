package com.chernik.internetprovider.servlet;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.command.HttpRequestParameter;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import com.chernik.internetprovider.servlet.command.commandimpl.errorcommand.ErrorCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServlet extends HttpServlet {
    private final static Logger LOGGER = LogManager.getLogger(FrontControllerServlet.class);

    private CommandHandler commandHandler;
    private ErrorCommand errorCommand;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initHandler();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        initHandler();
    }

    private void initHandler() {
        commandHandler = (CommandHandler) getServletContext().getAttribute("commandHandler");
        errorCommand = (ErrorCommand) getServletContext().getAttribute("errorCommand");
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.TRACE, "Request: {}, method: {} came", request.getRequestURI(), request.getMethod());
        try {
            HttpRequestParameter parameter = new HttpRequestParameter(request.getRequestURI().toLowerCase(),
                    HttpRequestType.valueOf(request.getMethod()));
            Command command = commandHandler.getCommand(parameter);
            command.execute(request, response);
        } catch (BaseException e) {
            LOGGER.log(Level.WARN, "Request: {}, method: {} does not support", request.getRequestURI(), request.getMethod());
            error(e, request, response);
        }
    }


    private void error(BaseException error, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            errorCommand.execute(request, response, error);
        } catch (BaseException e) {
            LOGGER.log(Level.ERROR, "Error {} does not support", e.getStatusCode());
        }
    }
}
