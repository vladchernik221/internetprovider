package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand;

import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/loginPage", method = HttpRequestType.GET)
public class LoginCommandGet implements Command {
    private final static Logger LOGGER = LogManager.getLogger(LoginCommandGet.class);

    private final static String LOGIN_PAGE = "/WEB-INF/jsp/loginPage.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(LOGIN_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", LOGIN_PAGE);
        dispatcher.forward(request, response);
    }
}
