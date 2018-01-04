package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff_plan/{\\d+}", method = HttpRequestType.GET)
public class TariffPlanByIdCommandGet implements Command {
    private final static String TARIFF_PLAN_PAGE = "/WEB-INF/jsp/tariff.jsp";

    @Autowired
    private TariffPlanService tariffPlanService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        TariffPlan tariffPlan = tariffPlanService.getTariffPlan(id);

        request.setAttribute("tariffPlan", tariffPlan);
        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_PLAN_PAGE);
        dispatcher.forward(request, response);
    }
}
