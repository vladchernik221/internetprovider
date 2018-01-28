package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TariffPlanMapper extends BaseMapper implements Mapper<TariffPlan> {

    private static final String DISCOUNT_ID_DELIMITER = ";";

    @Override
    public TariffPlan create(HttpServletRequest request) throws BadRequestException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setName(getMandatoryString(request, "name"));
        tariffPlan.setDescription(getNotMandatoryString(request, "description"));
        tariffPlan.setDownSpeed(getMandatoryInt(request, "downSpeed"));
        tariffPlan.setUpSpeed(getMandatoryInt(request, "upSpeed"));
        tariffPlan.setIncludedTraffic(getNotMandatoryInt(request, "includedTraffic"));
        tariffPlan.setPriceOverTraffic(getNotMandatoryBigDecimal(request, "priceOverTraffic"));
        tariffPlan.setMonthlyFee(getMandatoryBigDecimal(request, "monthlyFee"));

        List<Discount> discounts = Arrays.stream(request.getParameter("discounts").split(DISCOUNT_ID_DELIMITER))
                .filter(id -> !id.isEmpty())
                .map(id -> new Discount(Long.valueOf(id)))
                .collect(Collectors.toList());
        tariffPlan.setDiscounts(discounts);
        return tariffPlan;
    }
}
