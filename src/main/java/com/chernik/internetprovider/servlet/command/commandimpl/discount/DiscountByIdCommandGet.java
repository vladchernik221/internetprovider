package com.chernik.internetprovider.servlet.command.commandimpl.discount;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/discount/{\\d+}", method = RequestType.GET)
public class DiscountByIdCommandGet implements Command {
    private static final String DISCOUNT_PAGE = "/WEB-INF/jsp/discount/discount.jsp";

    @Autowired
    private DiscountService discountService;

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        Discount discount = discountService.getById(id);

        request.setAttribute("discount", discount);
        RequestDispatcher dispatcher = request.getRequestDispatcher(DISCOUNT_PAGE);
        dispatcher.forward(request, response);
    }
}
