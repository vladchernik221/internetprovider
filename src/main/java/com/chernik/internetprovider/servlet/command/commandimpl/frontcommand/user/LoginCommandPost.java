package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.user;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.property.PropertyHolder;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@HttpRequestProcessor(uri = "/signin", method = HttpRequestType.POST)
public class LoginCommandPost implements Command {
    private final static Logger LOGGER = LogManager.getLogger(LoginCommandPost.class);

    private final static String INDEX_PAGE = "/";

    @Autowired
    private UserService userService;
    @Autowired
    private PropertyHolder propertyHolder;

    private int sessionTimeout = 1_800;

    @AfterCreate
    public void initParameters() {
        String sessionTimeoutProperty = propertyHolder.getProperty("session.timeOut");
        if (sessionTimeoutProperty != null) {
            sessionTimeout = Integer.valueOf(sessionTimeoutProperty);
        }
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Optional<User> user = userService.authenticate(login, password);

        if (user.isPresent() && !user.get().getBlocked()) {
            LOGGER.log(Level.TRACE, "Successful authentication user: {}", user.get().getLogin());
            HttpSession session = request.getSession();
            session.setAttribute("user", user.get());
            session.setMaxInactiveInterval(sessionTimeout);
            response.sendRedirect(INDEX_PAGE);
        } else if (user.isPresent()) {
            LOGGER.log(Level.TRACE, "Not successful authentication user: {} is blocked", user.get().getLogin());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User is blocked.");//TODO Is it really need?
        } else {
            LOGGER.log(Level.TRACE, "Not successful authentication user: {} not found", login);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User name or password is wrong.");//TODO Is it really need?
        }
    }
}
