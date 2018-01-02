package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand;

import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import com.chernik.internetprovider.context.HttpRequestProcessor;
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
    private final static Logger LOGGER = LogManager.getLogger(TestCommandGet.class);

    private final static String TEST_FOUND_JSP = "/WEB-INF/jsp/test.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(TEST_FOUND_JSP);
        LOGGER.log(Level.TRACE, "Forward to pageNumber: {}", TEST_FOUND_JSP);
        dispatcher.forward(request, response);
    }
}
