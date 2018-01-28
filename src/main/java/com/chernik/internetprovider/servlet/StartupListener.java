package com.chernik.internetprovider.servlet;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.servlet.command.CommandHandler;
import com.chernik.internetprovider.servlet.filter.SecurityConfigHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {
    private ContextInitializer contextInitializer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        contextInitializer = ContextInitializer.getInstance();
        CommandHandler commandHandler = contextInitializer.getComponent(CommandHandler.class);
        servletContextEvent.getServletContext().setAttribute("commandHandler", commandHandler);
        SecurityConfigHandler securityConfigHandler = contextInitializer.getComponent(SecurityConfigHandler.class);
        servletContextEvent.getServletContext().setAttribute("securityHandler", securityConfigHandler);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        contextInitializer.destroy();
    }
}
