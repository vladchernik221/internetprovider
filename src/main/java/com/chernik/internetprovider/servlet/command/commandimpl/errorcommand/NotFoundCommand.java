package com.chernik.internetprovider.servlet.command.commandimpl.errorcommand;

import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "404")
public class NotFoundCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger(NotFoundCommand.class);

    private final static String NOT_FOUND_JSP = "/WEB-INF/jsp/404.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(NOT_FOUND_JSP);
        LOGGER.log(Level.TRACE, "Forward to page: {}", NOT_FOUND_JSP);
        dispatcher.forward(request, response);
    }
}
