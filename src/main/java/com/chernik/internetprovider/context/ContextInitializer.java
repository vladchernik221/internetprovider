package com.chernik.internetprovider.context;

import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.ConnectionPoolImpl;
import com.chernik.internetprovider.persistence.dao.UserDao;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import com.chernik.internetprovider.servicelayer.service.AuthenticationService;
import com.chernik.internetprovider.servicelayer.serviceImpl.AuthenticationServiceImpl;
import com.chernik.internetprovider.servlet.command.*;
import com.chernik.internetprovider.servlet.command.commandimpl.errorcommand.NotFoundCommand;
import com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.AuthenticationCommandPost;
import com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.LoginCommandGet;
import com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.TestCommandGet;

import javax.servlet.ServletContextEvent;

public class ContextInitializer {
    private ConnectionPool connectionPool = new ConnectionPoolImpl();
    private CommandHandler commandHandler = new FrontCommandHandlerImpl();
    private CommandHandler errorHandler = new ErrorCommandHandlerImpl();

    public void initialize(ServletContextEvent servletContextEvent) {
        initHandler();
        initErrorHandler();
        initServletContext(servletContextEvent);
    }

    public void destroy() {
        connectionPool.closeAllConnections();
    }

    private void initServletContext(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("commandHandler", commandHandler);
        servletContextEvent.getServletContext().setAttribute("errorHandler", errorHandler);
    }

    private void initHandler() {
        commandHandler.registerCommand(new HttpRequestParameter("/test", HttpRequestType.GET),
                new TestCommandGet());
        commandHandler.registerCommand(new HttpRequestParameter("/loginPage", HttpRequestType.GET),
                new LoginCommandGet());

        UserRepository userRepository = new UserDao(connectionPool);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository);
        commandHandler.registerCommand(new HttpRequestParameter("/authentication", HttpRequestType.POST),
                new AuthenticationCommandPost(authenticationService));
    }

    private void initErrorHandler() {
        errorHandler.registerCommand(new HttpRequestParameter("404"), new NotFoundCommand());
    }

}
