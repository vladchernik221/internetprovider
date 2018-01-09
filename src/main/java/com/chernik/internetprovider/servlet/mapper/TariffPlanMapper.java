package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import javax.servlet.http.HttpServletRequest;

@Component
public class TariffPlanMapper extends Mapper<TariffPlan>{

    @Override
    public TariffPlan create(HttpServletRequest request) throws BadRequestException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setName(getMandatoryString(request.getParameter("name")));
        tariffPlan.setDescription(getNotMandatoryString(request.getParameter("description")));
        tariffPlan.setDownSpeed(getMandatoryInt(request.getParameter("downSpeed")));
        tariffPlan.setUpSpeed(getMandatoryInt(request.getParameter("upSpeed")));
        tariffPlan.setIncludedTraffic(getNotMandatoryInt(request.getParameter("includedTraffic")));
        tariffPlan.setPriceOverTraffic(getNotMandatoryInt(request.getParameter("priceOverTraffic")));
        tariffPlan.setMonthlyFee(getBigDecimal(request.getParameter("monthlyFee")));
        return tariffPlan;
    }
}
