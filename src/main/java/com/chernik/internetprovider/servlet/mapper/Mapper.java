package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.service.RegularExpressionService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.function.Function;

//TODO parameters names to constants somewhere
//TODO refactor to lambda
public abstract class Mapper<T> {
    private static final String INTEGER_FORMAT_REGULAR_EXPRESSION = "^\\d+$";
    private static final String DOUBLE_FORMAT_REGULAR_EXPRESSION = "^\\d{1,15}(\\.\\d{1,2})?$";
    private static final String DATE_FORMAT_REGULAR_EXPRESSION = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String BOOLEAN_FORMAT_REGULAR_EXPRESSION = "^(true|false)$";

    @Autowired
    private RegularExpressionService regularExpressionService;


    public abstract T create(HttpServletRequest request) throws BadRequestException;

    String getMandatoryString(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            return data;
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }

    String getNotMandatoryString(String data) {
        return data != null && !data.isEmpty() ? data : null;
    }

    Integer getMandatoryInt(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
                return Integer.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }

    Integer getNotMandatoryInt(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
                return Integer.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            return null;
        }
    }

    BigDecimal getMandatoryBigDecimal(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, DOUBLE_FORMAT_REGULAR_EXPRESSION)) {
                return BigDecimal.valueOf(Double.valueOf(data));
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }

    BigDecimal getNotMandatoryBigDecimal(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, DOUBLE_FORMAT_REGULAR_EXPRESSION)) {
                return BigDecimal.valueOf(Double.valueOf(data));
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            return null;
        }
    }

    Long getMandatoryLong(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
                return Long.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            throw new BadRequestException("Mandatory field does not initialize");
        }
    }

    Date getMandatoryDate(HttpServletRequest request, String parameterName) throws BadRequestException {
        return getParameter(request, parameterName, DATE_FORMAT_REGULAR_EXPRESSION, true, Date::valueOf);
    }

    Boolean getNotMandatoryBoolean(String data) throws BadRequestException {
        if (data != null && !data.isEmpty()) {
            if (regularExpressionService.checkTo(data, BOOLEAN_FORMAT_REGULAR_EXPRESSION)) {
                return Boolean.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            return null;
        }
    }

    private <P> P getParameter(HttpServletRequest request, String parameterName, String formatRegularExpression, boolean mandatory, Function<String, P> convertFunction) throws BadRequestException {
        String data = request.getParameter(parameterName);
        if (data != null) {
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
