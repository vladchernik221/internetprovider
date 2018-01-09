package com.chernik.internetprovider.servlet.command.commandimpl.test;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/test", method = HttpRequestType.GET)
public class TestCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(TestCommandGet.class);

    private static final String TEST_FOUND_JSP = "/WEB-INF/jsp/test.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, BaseException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(TEST_FOUND_JSP);
        LOGGER.log(Level.TRACE, "Forward to page: {}", TEST_FOUND_JSP);
        //dispatcher.forward(request, response);
        throw new UnsupportedOperationException("123");
    }
}