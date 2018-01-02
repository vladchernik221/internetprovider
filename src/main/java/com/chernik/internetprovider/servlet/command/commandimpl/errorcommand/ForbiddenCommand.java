package com.chernik.internetprovider.servlet.command.commandimpl.errorcommand;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.servlet.command.Command;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "403")
public class ForbiddenCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger(ForbiddenCommand.class);

    private final static String FORBIDDEN_JSP = "/WEB-INF/jsp/403.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(FORBIDDEN_JSP);
        LOGGER.log(Level.TRACE, "Forward to pageNumber: {}", FORBIDDEN_JSP);
        dispatcher.forward(request, response);
    }
}
