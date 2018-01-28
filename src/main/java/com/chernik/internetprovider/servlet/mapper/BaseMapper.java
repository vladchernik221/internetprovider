package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.service.RegularExpressionService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.function.Function;

@Component
public class BaseMapper {
    private static final String INTEGER_FORMAT_REGULAR_EXPRESSION = "^\\d+$";
    private static final String DOUBLE_FORMAT_REGULAR_EXPRESSION = "^\\d{1,15}(\\.\\d{1,2})?$";
    private static final String DATE_FORMAT_REGULAR_EXPRESSION = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String BOOLEAN_FORMAT_REGULAR_EXPRESSION = "^(true|false)$";

    @Autowired
    private RegularExpressionService regularExpressionService;

    public void setRegularExpressionService(RegularExpressionService regularExpressionService) {
        this.regularExpressionService = regularExpressionService;
    }

    public String getMandatoryString(HttpServletRequest request, String parameterName) throws BadRequestException {
        String data = request.getParameter(parameterName);
        if (data != null && !data.isEmpty()) {
            return data;
        } else {
            throw new BadRequestException(String.format("Mandatory parameter %s does not initialize", parameterName));
        }
    }

    public String getNotMandatoryString(HttpServletRequest request, String parameterName) {
        String data = request.getParameter(parameterName);
        return data != null && !data.isEmpty() ? data : null;
    }

    public Integer getMandatoryInt(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, INTEGER_FORMAT_REGULAR_EXPRESSION, true, Integer::valueOf);
    }

    public Integer getNotMandatoryInt(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, INTEGER_FORMAT_REGULAR_EXPRESSION, false, Integer::valueOf);
    }

    public BigDecimal getMandatoryBigDecimal(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, DOUBLE_FORMAT_REGULAR_EXPRESSION, true, x -> BigDecimal.valueOf(Double.valueOf(x)));
    }

    public BigDecimal getNotMandatoryBigDecimal(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, DOUBLE_FORMAT_REGULAR_EXPRESSION, false, x -> BigDecimal.valueOf(Double.valueOf(x)));
    }

    public Long getMandatoryLong(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, INTEGER_FORMAT_REGULAR_EXPRESSION, true, Long::valueOf);
    }

    public Long getNotMandatoryLong(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, INTEGER_FORMAT_REGULAR_EXPRESSION, false, Long::valueOf);
    }

    public Date getMandatoryDate(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, DATE_FORMAT_REGULAR_EXPRESSION, true, Date::valueOf);
    }

    public Boolean getNotMandatoryBoolean(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, BOOLEAN_FORMAT_REGULAR_EXPRESSION, false, Boolean::valueOf);
    }

    private <P> P getParameter(HttpServletRequest request, String parameterName, String formatRegularExpression, boolean mandatory, Function<String, P> convertFunction) throws BadRequestException {
        String data = request.getParameter(parameterName);
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, formatRegularExpression)) {
                return convertFunction.apply(data);
            } else {
                throw new BadRequestException(String.format("Parameter %s: %s have wrong format", parameterName, data));
            }
        } else {
            if (!mandatory) {
                return null;
            } else {
                throw new BadRequestException(String.format("Mandatory parameter %s does not initialize", parameterName));
            }
        }
    }
}
