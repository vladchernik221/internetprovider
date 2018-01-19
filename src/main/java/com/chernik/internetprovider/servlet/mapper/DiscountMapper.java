package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Discount;

import javax.servlet.http.HttpServletRequest;

@Component
public class DiscountMapper extends Mapper<Discount> {

    @Override
    public Discount create(HttpServletRequest request) throws BadRequestException {
        Discount discount = new Discount();
        discount.setName(getMandatoryString(request.getParameter("name")));
        discount.setDescription(getNotMandatoryString(request.getParameter("description")));
        discount.setAmount(getMandatoryInt(request.getParameter("amount")));
        discount.setStartDate(getMandatoryDate(request, "startDate"));
        discount.setEndDate(getMandatoryDate(request, "endDate"));

        Boolean onlyForNewClient = getNotMandatoryBoolean(request.getParameter("onlyForNewClient"));
        if (onlyForNewClient == null) {
            onlyForNewClient = false;
        }
        discount.setOnlyForNewClient(onlyForNewClient);
        return discount;
    }
}
