package com.chernik.internetprovider.servlet.command.impl.service;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/service/{\\d+}", method = RequestType.GET)
public class ServiceByIdCommandGet implements Command {
    private static final String SERVICE_PAGE = "/WEB-INF/jsp/service/service.jsp";

    @Autowired
    private ServiceService serviceService;

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        Service service = serviceService.getById(id);

        request.setAttribute("service", service);
        RequestDispatcher dispatcher = request.getRequestDispatcher(SERVICE_PAGE);
        dispatcher.forward(request, response);
    }
}
