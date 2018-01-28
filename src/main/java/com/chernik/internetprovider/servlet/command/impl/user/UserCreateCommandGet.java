package com.chernik.internetprovider.servlet.command.impl.user;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@HttpRequestProcessor(uri = "/user/new", method = RequestType.GET)
public class UserCreateCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(UserCreateCommandGet.class);

    private static final String USER_FORM_PAGE = "/WEB-INF/jsp/user/userForm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(USER_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", USER_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
