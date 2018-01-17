package com.chernik.internetprovider.servlet.command.commandimpl.discount;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@HttpRequestProcessor(uri = "/discount/{\\d+}/edit", method = RequestType.GET)
public class DiscountEditCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(DiscountEditCommandGet.class);

    private static final String DISCOUNT_FORM_PAGE = "/WEB-INF/jsp/discount/discountForm.jsp";

    @Autowired
    private DiscountService discountService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        Discount discount = discountService.getById(id);

        request.setAttribute("discount", discount);
        RequestDispatcher dispatcher = request.getRequestDispatcher(DISCOUNT_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
