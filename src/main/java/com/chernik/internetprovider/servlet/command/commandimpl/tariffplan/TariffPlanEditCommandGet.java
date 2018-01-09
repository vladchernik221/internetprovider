package com.chernik.internetprovider.servlet.command.commandimpl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff-plan/{\\d+}/edit", method = RequestType.GET)
public class TariffPlanEditCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanEditCommandGet.class);

    private static final String TARIFF_FORM_PAGE = "/WEB-INF/jsp/tariffForm.jsp";

    @Autowired
    private TariffPlanService tariffPlanService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        TariffPlan tariffPlan = tariffPlanService.getById(id);

        request.setAttribute("tariffPlan", tariffPlan);
        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
