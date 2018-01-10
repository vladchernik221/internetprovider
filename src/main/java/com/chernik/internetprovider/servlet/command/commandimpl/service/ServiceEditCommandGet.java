package com.chernik.internetprovider.servlet.command.commandimpl.service;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@HttpRequestProcessor(uri = "/service/{\\d+}/edit", method = RequestType.GET)
public class ServiceEditCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ServiceEditCommandGet.class);

    private static final String SERVICE_FORM_PAGE = "/WEB-INF/jsp/service/serviceForm.jsp";

    @Autowired
    private ServiceService serviceService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        Service service = serviceService.getById(id);

        request.setAttribute("service", service);
        RequestDispatcher dispatcher = request.getRequestDispatcher(SERVICE_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
