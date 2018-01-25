package com.chernik.internetprovider.servlet.command.commandimpl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@HttpRequestProcessor(uri = "/tariff-plan/{\\d+}/archive", method = RequestType.POST)
public class TariffPlanArchiveCommandPost implements Command {

    @Autowired
    private TariffPlanService tariffPlanService;

    public void setTariffPlanService(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);
        tariffPlanService.archive(id);
    }
}
