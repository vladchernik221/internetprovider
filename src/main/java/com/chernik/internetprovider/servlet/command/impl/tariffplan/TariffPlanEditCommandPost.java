package com.chernik.internetprovider.servlet.command.impl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.TariffPlanMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@HttpRequestProcessor(uri = "/tariff-plan/{\\d+}", method = RequestType.POST)
public class TariffPlanEditCommandPost implements Command {

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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);
        TariffPlan tariffPlan = tariffPlanMapper.create(request);
        tariffPlan.setTariffPlanId(id);

        tariffPlanService.update(tariffPlan);
    }
}
