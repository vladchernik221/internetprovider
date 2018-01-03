package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.AuthenticationService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@HttpRequestProcessor(uri = "/signin", method = HttpRequestType.POST)
public class SigninCommandPost implements Command {
    private final static Logger LOGGER = LogManager.getLogger(SigninCommandPost.class);

    @Autowired
    private AuthenticationService authenticationService;

    public SigninCommandPost() {
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = authenticationService.authenticate(login, password);

        if (user != null && !user.getBlocked()) {
            LOGGER.log(Level.TRACE, "Successful authentication user: {}", user.getLogin());
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(1_800);//TODO to properties

            response.sendRedirect(request.getHeader("Referer"));//TODO constant
        } else if (user != null) {
            LOGGER.log(Level.TRACE, "Not successful authentication user: {} is blocked", user.getLogin());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>User is blocked.</font>");
            dispatcher.include(request, response);
        } else {
            LOGGER.log(Level.TRACE, "Not successful authentication user: {} not found", login);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>Either user name or password is wrong.</font>");
            //dispatcher.include(request, response);
        }
    }
}
