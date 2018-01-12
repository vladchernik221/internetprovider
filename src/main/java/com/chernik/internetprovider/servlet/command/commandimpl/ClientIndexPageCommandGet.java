package com.chernik.internetprovider.servlet.command.commandimpl;

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

@HttpRequestProcessor(uri = "/client", method = RequestType.GET)
public class ClientIndexPageCommandGet implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ClientIndexPageCommandGet.class);

    private static final String CLIENT_INDEX_PAGE = "/WEB-INF/jsp/client-index.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(CLIENT_INDEX_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", CLIENT_INDEX_PAGE);
        dispatcher.forward(request, response);
    }
}
