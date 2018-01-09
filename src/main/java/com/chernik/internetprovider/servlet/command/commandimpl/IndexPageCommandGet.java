package com.chernik.internetprovider.servlet.command.commandimpl;

import com.chernik.internetprovider.context.HttpRequestProcessor;
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

@HttpRequestProcessor(uri = "/", method = HttpRequestType.GET)
public class IndexPageCommandGet implements Command {

    private static final Logger LOGGER = LogManager.getLogger(IndexPageCommandGet.class);

    private static final String INDEX_PAGE = "/WEB-INF/jsp/index.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(INDEX_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", INDEX_PAGE);
        dispatcher.forward(request, response);
    }
}
