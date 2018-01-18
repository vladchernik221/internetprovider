package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

@Component
public class TariffPlanMapper extends Mapper<TariffPlan>{

    private static final String DISCOUNT_ID_DELIMITER = ";";

    @Override
    public TariffPlan create(HttpServletRequest request) throws BadRequestException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setName(getMandatoryString(request.getParameter("name")));
        tariffPlan.setDescription(getNotMandatoryString(request.getParameter("description")));
        tariffPlan.setDownSpeed(getMandatoryInt(request.getParameter("downSpeed")));
        tariffPlan.setUpSpeed(getMandatoryInt(request.getParameter("upSpeed")));
        tariffPlan.setIncludedTraffic(getNotMandatoryInt(request.getParameter("includedTraffic")));
        tariffPlan.setPriceOverTraffic(getNotMandatoryBigDecimal(request.getParameter("priceOverTraffic")));
        tariffPlan.setMonthlyFee(getMandatoryBigDecimal(request.getParameter("monthlyFee")));

        List<Discount> discounts = Arrays.stream(request.getParameter("discounts").split(DISCOUNT_ID_DELIMITER))
                .map(id -> {
                    Discount discount = new Discount();
                    discount.setDiscountId(Long.valueOf(id));
                    return discount;
                })
                .collect(Collectors.toList());
        tariffPlan.setDiscounts(discounts);
        return tariffPlan;
    }
}
