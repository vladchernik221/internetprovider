package com.chernik.internetprovider.servlet.command.commandimpl.service;

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

@HttpRequestProcessor(uri = "/service/new", method = RequestType.GET)
public class ServiceCreateCommandGet implements Command{
    private static final Logger LOGGER = LogManager.getLogger(ServiceCreateCommandGet.class);

    private static final String SERVICE_FORM_PAGE = "/WEB-INF/jsp/service/serviceForm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(SERVICE_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", SERVICE_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
