package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff_plan/{\\d+}/archive", method = HttpRequestType.POST)
public class TariffPlanArchiveCommandPost implements Command {

    @Autowired
    private TariffPlanService tariffPlanService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);
        tariffPlanService.archiveTariffPlan(id);
    }
}
