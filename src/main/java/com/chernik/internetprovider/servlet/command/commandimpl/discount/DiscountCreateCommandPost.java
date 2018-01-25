package com.chernik.internetprovider.servlet.command.commandimpl.discount;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.DiscountMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/discount/new", method = RequestType.POST)
public class DiscountCreateCommandPost implements Command {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountMapper discountMapper;

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setDiscountMapper(DiscountMapper discountMapper) {
        this.discountMapper = discountMapper;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        Discount discount = discountMapper.create(request);
        Long generatedId = discountService.create(discount);
        response.getWriter().write(generatedId.toString());
    }
}