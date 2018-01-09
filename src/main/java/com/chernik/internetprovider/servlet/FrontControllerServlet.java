package com.chernik.internetprovider.servlet;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.command.HttpRequestParameter;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(FrontControllerServlet.class);

    private CommandHandler commandHandler;

    @Override
    public void init() throws ServletException {
        super.init();
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
        } catch (UnableSaveEntityException e) {
            response.setStatus(e.getStatusCode());
            response.getWriter().write(e.getMessage());
        } catch (BaseException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            response.sendError(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.WARN, e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
