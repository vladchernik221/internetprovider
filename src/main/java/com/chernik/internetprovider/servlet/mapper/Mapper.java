package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.service.RegularExpressionService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public abstract class Mapper<T> {
    private static final String INTEGER_FORMAT_REGULAR_EXPRESSION = "^\\d+$";
    private static final String DOUBLE_FORMAT_REGULAR_EXPRESSION = "^\\d{1,15}(\\.\\d{1,2})*$";


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
            if (regularExpressionService.checkToRegularExpression(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
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
            if (regularExpressionService.checkToRegularExpression(data, INTEGER_FORMAT_REGULAR_EXPRESSION)) {
                return Integer.valueOf(data);
            } else {
                throw new BadRequestException(String.format("Field: %s have wrong format", data));
            }
        } else {
            return null;
        }
    }

    BigDecimal getBigDecimal(String data) throws BadRequestException {
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
