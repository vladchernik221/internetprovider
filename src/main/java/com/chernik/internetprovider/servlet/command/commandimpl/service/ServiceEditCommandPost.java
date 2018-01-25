package com.chernik.internetprovider.servlet.command.commandimpl.service;

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

@HttpRequestProcessor(uri = "/service/{\\d+}", method = RequestType.POST)
public class ServiceEditCommandPost implements Command {

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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);
        Service service = serviceMapper.create(request);
        service.setServiceId(id);

        serviceService.update(service);
    }
}
