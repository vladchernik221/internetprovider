package com.chernik.internetprovider.servlet;

import com.chernik.internetprovider.exception.FrontControllerException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.command.HttpRequestParameter;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
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
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.TRACE, "Request: {}, method: {} came", request.getRequestURI(), request.getMethod());
        try {
            HttpRequestParameter parameter = new HttpRequestParameter(request.getRequestURI().toLowerCase(),
                    HttpRequestType.valueOf(request.getMethod()));
            Command command = commandHandler.getCommand(parameter);
            command.execute(request, response);
        } catch (FrontControllerException e) {
            LOGGER.log(Level.TRACE, "Request: {}, method: {} does not support", request.getRequestURI(), request.getMethod());
            error(e.getStatusCode(), request, response);
        }
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpRequestParameter parameter = new HttpRequestParameter(error);
            Command command = commandHandler.getCommand(parameter);
            command.execute(request, response);
        } catch (FrontControllerException e) {
            LOGGER.log(Level.ERROR, "Error {} does not support", error);
        }
    }
}
