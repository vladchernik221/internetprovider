package com.chernik.internetprovider.servlet.command.impl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.TariffPlanMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//TODO create unlimited tariff plan exception
@HttpRequestProcessor(uri = "/tariff-plan/new", method = RequestType.POST)
public class TariffPlanCreateCommandPost implements Command {

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private TariffPlanMapper tariffPlanMapper;

    public void setTariffPlanService(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    public void setTariffPlanMapper(TariffPlanMapper tariffPlanMapper) {
        this.tariffPlanMapper = tariffPlanMapper;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        TariffPlan tariffPlan = tariffPlanMapper.create(request);
        Long generatedId = tariffPlanService.create(tariffPlan);
        response.getWriter().write(generatedId.toString());
    }
}