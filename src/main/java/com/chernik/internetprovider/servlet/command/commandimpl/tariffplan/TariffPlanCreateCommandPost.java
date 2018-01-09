package com.chernik.internetprovider.servlet.command.commandimpl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import com.chernik.internetprovider.servlet.mapper.TariffPlanMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff-plan/new", method = HttpRequestType.POST)
public class TariffPlanCreateCommandPost implements Command {

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private TariffPlanMapper tariffPlanMapper;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        TariffPlan tariffPlan = tariffPlanMapper.create(request);
        tariffPlan.setArchived(false);

        Long generatedId = tariffPlanService.createNewTariffPlan(tariffPlan);
        response.getWriter().write(generatedId.toString());
    }
}