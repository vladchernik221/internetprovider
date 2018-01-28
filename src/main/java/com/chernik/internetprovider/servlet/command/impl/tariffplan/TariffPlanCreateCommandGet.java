package com.chernik.internetprovider.servlet.command.impl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@HttpRequestProcessor(uri = "/tariff-plan/new", method = RequestType.GET)
public class TariffPlanCreateCommandGet implements Command{
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanCreateCommandGet.class);

    private static final String TARIFF_FORM_PAGE = "/WEB-INF/jsp/tariff/tariffForm.jsp";

    @Autowired
    private DiscountService discountService;

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, BaseException {
        List<Discount> discounts = discountService.getAll();
        request.setAttribute("discounts", discounts);

        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", TARIFF_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
