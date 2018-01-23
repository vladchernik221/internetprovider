package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.command.RequestParameter;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(LoginFilter.class);
    private SecurityConfigHandler securityConfigHandler;
    private CommandHandler commandHandler;

    @Override
    public void init(FilterConfig filterConfig) {
        securityConfigHandler = (SecurityConfigHandler) filterConfig.getServletContext().getAttribute("securityHandler");
        commandHandler = (CommandHandler) filterConfig.getServletContext().getAttribute("commandHandler");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(true);

        User user = (User) session.getAttribute("user");
        UserRole userRole = user != null ? user.getUserRole() : UserRole.UNAUTHORIZED;
        boolean isAvailable = securityConfigHandler.isAvailable(RequestType.valueOf(request.getMethod()),
                request.getRequestURI(), userRole);

        if (isAvailable) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            try {
                commandHandler.getCommand(new RequestParameter("403")).execute(request, response);
            } catch (BaseException e) {
                LOGGER.log(Level.ERROR, "Error {} does not support", e.getStatusCode());
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error {} does not support");
            }
        }
    }

    @Override
    public void destroy() {}
}
