package com.chernik.internetprovider.servlet.command.impl.discount;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/discount", method = RequestType.GET)
public class DiscountListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(DiscountListCommandGet.class);

    private static final String DISCOUNT_LIST_PAGE = "/WEB-INF/jsp/discount/discountList.jsp";

    @Autowired
    private DiscountService discountService;

    @Autowired
    private BaseMapper baseMapper;

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, BaseException, IOException {
        Integer pageNumber = baseMapper.getNotMandatoryInt(request, "page");
        pageNumber = (pageNumber != null) ? pageNumber - 1 : 0;

        RequestDispatcher dispatcher = request.getRequestDispatcher(DISCOUNT_LIST_PAGE);
        Page<Discount> discountsPage = discountService.getPage(new Pageable(pageNumber, 10));
        request.setAttribute("discountsPage", discountsPage);
        LOGGER.log(Level.TRACE, "Forward to page: {}", DISCOUNT_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
