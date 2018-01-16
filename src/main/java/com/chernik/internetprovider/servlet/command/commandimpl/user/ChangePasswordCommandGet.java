package com.chernik.internetprovider.servlet.command.commandimpl.user;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/user/{\\d+}/password", method = RequestType.GET)
public class ChangePasswordCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ChangePasswordCommandGet.class);

    private static final String CHANGE_PASSWORD_PAGE = "/WEB-INF/jsp/user/changePassword.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(CHANGE_PASSWORD_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", CHANGE_PASSWORD_PAGE);
        dispatcher.forward(request, response);
    }
}
