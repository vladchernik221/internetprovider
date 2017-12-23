package com.chernik.internetprovider.servlet;

import com.chernik.internetprovider.context.ContextInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {
    private ContextInitializer contextInitializer = new ContextInitializer();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        contextInitializer.initialize(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        contextInitializer.destroy();
    }
}
