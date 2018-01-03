package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.command.HttpRequestParameter;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    private final static Logger LOGGER = LogManager.getLogger(LoginFilter.class);
    private SecurityConfigHandler securityConfigHandler;
    private CommandHandler errorHandler;

    @Override
    public void init(FilterConfig filterConfig) {
        securityConfigHandler = (SecurityConfigHandler) filterConfig.getServletContext().getAttribute("securityHandler");
        errorHandler = (CommandHandler) filterConfig.getServletContext().getAttribute("commandHandler");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(true);

        User user = (User) session.getAttribute("user");
        UserRole userRole = user != null ? user.getUserRole() : UserRole.UNAUTHORIZED;
        boolean isAvailable = securityConfigHandler.isAvailable(HttpRequestType.valueOf(request.getMethod()),
                request.getRequestURI(), userRole);

        if (isAvailable) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            try {
                errorHandler.getCommand(new HttpRequestParameter("403")).execute(request, response);
            } catch (BaseException e) {
                e.printStackTrace();//TODO exception
            }
        }
    }

    @Override
    public void destroy() {

    }
}
