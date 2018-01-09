package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff_plan", method = HttpRequestType.GET)
public class ListTariffPlansCommandGet implements Command {
    private final static Logger LOGGER = LogManager.getLogger(ListTariffPlansCommandGet.class);

    private final static String TARIFF_LIST_PAGE = "/WEB-INF/jsp/tariffList.jsp";

    @Autowired
    private TariffPlanRepository tariffPlanService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, TimeOutException {
        int pageNumber = 0;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page")) - 1;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_LIST_PAGE);
        Page<TariffPlan> tariffPlansPage = tariffPlanService.getTariffPlanPage(false, new Pageable(pageNumber, 10));//TODO to property or constant or somewhere
        request.setAttribute("tariffPlansPage", tariffPlansPage);
        LOGGER.log(Level.TRACE, "Forward to page: {}", TARIFF_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
