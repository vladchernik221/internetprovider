package com.chernik.internetprovider.servlet.command.impl;

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

@HttpRequestProcessor(uri = "/about", method = RequestType.GET)
public class AboutPageCommandGet implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AboutPageCommandGet.class);

    private static final String ABOUT_PAGE = "/WEB-INF/jsp/about.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ABOUT_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", ABOUT_PAGE);
        dispatcher.forward(request, response);
    }
}
