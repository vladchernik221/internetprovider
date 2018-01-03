package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.RegularExpressionService;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@HttpRequestProcessor(uri = "/tariff_plan/new", method = HttpRequestType.POST)
public class NewTariffPlanPost implements Command {
    private final static String INTEGER_FORMAT_REGULAR_EXPRESSION = "^\\d+$";
    private final static String DOUBLE_FORMAT_REGULAR_EXPRESSION = "^\\d+(\\.\\d)*$";

    @Autowired
    private TariffPlanService tariffPlanService;
    @Autowired
    private RegularExpressionService regularExpressionService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws TimeOutException, DatabaseException {
        try {
            TariffPlan tariffPlan = new TariffPlan();
            tariffPlan.setName(request.getParameter("name"));
            tariffPlan.setDescription(request.getParameter("description"));
            tariffPlan.setDownSpeed(Integer.valueOf(request.getParameter("downSpeed")));
            tariffPlan.setUpSpeed(Integer.valueOf(request.getParameter("upSpeed")));
            tariffPlan.setIncludedTraffic(Integer.valueOf(request.getParameter("includedTraffic")));
            tariffPlan.setPriceOverTraffic(Integer.valueOf(request.getParameter("priceOverTraffic")));
            tariffPlan.setMonthlyFee(BigDecimal.valueOf(Double.valueOf(request.getParameter("monthlyFee"))));
            tariffPlanService.createNewTariffPlan(tariffPlan);
        } catch (UnableSaveEntityException e) {
            e.printStackTrace();
        }
    }
}
