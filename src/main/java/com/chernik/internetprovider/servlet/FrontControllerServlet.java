package com.chernik.internetprovider.servlet;

import com.chernik.internetprovider.exception.FrontControllerException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.command.HttpRequestParameter;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServlet extends HttpServlet {
    private CommandHandler commandHandler;
    private CommandHandler errorHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initHandlers();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        initHandlers();
    }

    private void initHandlers(){
        commandHandler = (CommandHandler) getServletContext().getAttribute("commandHandler");
        errorHandler = (CommandHandler) getServletContext().getAttribute("errorHandler");
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpRequestParameter parameter = new HttpRequestParameter(request.getRequestURI(),
                    HttpRequestType.valueOf(request.getMethod()));
            Command command = commandHandler.getCommand(parameter);
            command.execute(getServletContext(), request, response);
        } catch (FrontControllerException e) {
            error(e.getStatusCode(), request, response);
        }
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpRequestParameter parameter = new HttpRequestParameter(error);
            Command command = errorHandler.getCommand(parameter);
            command.execute(getServletContext(), request, response);
        } catch (FrontControllerException e) {
            e.printStackTrace();//TODO runtimeException
        }
    }
}
