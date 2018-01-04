package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BadRequestException;
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
import java.io.IOException;
import java.math.BigDecimal;

@HttpRequestProcessor(uri = "/tariff_plan/new", method = HttpRequestType.POST)
public class NewTariffPlanCommandPost implements Command {
    private final static String INTEGER_FORMAT_REGULAR_EXPRESSION = "^\\d+$";
    private final static String DOUBLE_FORMAT_REGULAR_EXPRESSION = "^\\d{1,15}(\\.\\d{1,2})*$";

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private RegularExpressionService regularExpressionService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws TimeOutException, DatabaseException, BadRequestException, IOException {
        try {
            TariffPlan tariffPlan = new TariffPlan();
            tariffPlan.setName(getMandatoryString(request.getParameter("name")));
            tariffPlan.setDescription(getNotMandatoryString(request.getParameter("description")));
            tariffPlan.setDownSpeed(getMandatoryInt(request.getParameter("downSpeed")));
            tariffPlan.setUpSpeed(getMandatoryInt(request.getParameter("upSpeed")));
            tariffPlan.setIncludedTraffic(getNotMandatoryInt(request.getParameter("includedTraffic")));
            tariffPlan.setIncludedTraffic(getNotMandatoryInt(request.getParameter("priceOverTraffic")));
            tariffPlan.setMonthlyFee(getBigDecimal(request.getParameter("monthlyFee")));
            tariffPlan.setArchived(false);
            tariffPlanService.createNewTariffPlan(tariffPlan);
        } catch (UnableSaveEntityException e) {
            response.setStatus(e.getStatusCode());
            response.getWriter().write(e.getMessage());
        }
    }

    private String getMandatoryString(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            return data;
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }

    private String getNotMandatoryString(String data) {
        return data != null && !data.isEmpty() ? data : null;
    }

    private Integer getMandatoryInt(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkToRegularExpression(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
                return Integer.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }

    private Integer getNotMandatoryInt(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkToRegularExpression(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
                return Integer.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            return null;
        }
    }

    private BigDecimal getBigDecimal(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkToRegularExpression(data, DOUBLE_FORMAT_REGULAR_EXPRESSION)) {
                return BigDecimal.valueOf(Double.valueOf(data));
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }
}
