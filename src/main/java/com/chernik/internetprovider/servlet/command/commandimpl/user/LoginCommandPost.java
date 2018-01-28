package com.chernik.internetprovider.servlet.command.commandimpl.user;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

@HttpRequestProcessor(uri = "/signin", method = RequestType.POST)
public class LoginCommandPost implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LoginCommandPost.class);

    private static final String INDEX_PAGE = "/";
    private static final String PROPERTY_FILE_NAME = "application";
    private static final int DEFAULT_SESSION_TIMEOUT = 1_800;

    @Autowired
    private UserService userService;

    @Autowired
    private BaseMapper baseMapper;

    private int sessionTimeout;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @AfterCreate
    public void initParameters() {
        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTY_FILE_NAME);
        String sessionTimeoutProperty = bundle.getString("session.timeOut");
        if (!sessionTimeoutProperty.isEmpty()) {
            sessionTimeout = Integer.valueOf(sessionTimeoutProperty);
        } else {
            sessionTimeout = DEFAULT_SESSION_TIMEOUT;
        }
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String login = baseMapper.getMandatoryString(request, "login");
        String password = baseMapper.getMandatoryString(request, "password");

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
            response.getWriter().write("User is blocked.");
        } else {
            LOGGER.log(Level.TRACE, "Not successful authentication user: {} not found", login);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User name or password is wrong.");
        }
    }
}
