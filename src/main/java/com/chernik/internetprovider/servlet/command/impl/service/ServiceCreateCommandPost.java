package com.chernik.internetprovider.servlet.command.impl.service;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.ServiceMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/service/new", method = RequestType.POST)
public class ServiceCreateCommandPost implements Command {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceMapper serviceMapper;

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public void setServiceMapper(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        Service service = serviceMapper.create(request);
        Long generatedId = serviceService.create(service);
        response.getWriter().write(generatedId.toString());
    }
}