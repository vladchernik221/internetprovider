package com.chernik.internetprovider.servlet.command.commandimpl.service;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/annex/{\\d+}/service/order", method = RequestType.GET)
public class ServiceAnnexListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ServiceAnnexListCommandGet.class);

    private static final String SERVICE_ANNEX_LIST_PAGE = "/WEB-INF/jsp/service/serviceAnnexList.jsp";

    @Autowired
    private ServiceService serviceService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, BaseException, IOException {
        int pageNumber = 0;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page")) - 1;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(SERVICE_ANNEX_LIST_PAGE);
        Page<Service> servicesPage = serviceService.getPage(new Pageable(pageNumber, 10), false);//TODO to property or constant or somewhere
        request.setAttribute("servicesPage", servicesPage);
        LOGGER.log(Level.TRACE, "Forward to page: {}", SERVICE_ANNEX_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
