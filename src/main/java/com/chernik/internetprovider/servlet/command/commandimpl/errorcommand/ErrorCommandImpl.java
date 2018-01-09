package com.chernik.internetprovider.servlet.command.commandimpl.errorcommand;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BaseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ErrorCommandImpl implements ErrorCommand {
    private final static Logger LOGGER = LogManager.getLogger(ErrorCommandImpl.class);

    private final static String ERROR_JSP = "/WEB-INF/jsp/error.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, BaseException e) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ERROR_JSP);
        request.setAttribute("statusCode", e.getStatusCode());
        request.setAttribute("message", e.getMessage());
        LOGGER.log(Level.TRACE, "Forward to error page. Status code: {}", e.getStatusCode());
        dispatcher.forward(request, response);
    }
}
