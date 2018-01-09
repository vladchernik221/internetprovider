package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Service;

import javax.servlet.http.HttpServletRequest;

@Component
public class ServiceMapper extends Mapper<Service> {

    @Override
    public Service create(HttpServletRequest request) throws BadRequestException {
        Service service = new Service();
        service.setName(getMandatoryString(request.getParameter("name")));
        service.setDescription(getNotMandatoryString(request.getParameter("description")));
        service.setPrice(getBigDecimal(request.getParameter("price")));
        return service;
    }
}
