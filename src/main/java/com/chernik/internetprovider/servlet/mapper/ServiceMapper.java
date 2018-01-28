package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Service;

import javax.servlet.http.HttpServletRequest;

@Component
public class ServiceMapper extends BaseMapper implements Mapper<Service> {

    @Override
    public Service create(HttpServletRequest request) throws BadRequestException {
        Service service = new Service();
        service.setName(getMandatoryString(request, "name"));
        service.setDescription(getNotMandatoryString(request, "description"));
        service.setPrice(getMandatoryBigDecimal(request, "price"));
        return service;
    }
}
