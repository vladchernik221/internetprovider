package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import com.chernik.internetprovider.servlet.mapper.TariffPlanMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff_plan/new", method = HttpRequestType.POST)
public class NewTariffPlanCommandPost implements Command {

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private TariffPlanMapper tariffPlanMapper;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws TimeOutException, DatabaseException, BadRequestException, IOException {
        try {
            TariffPlan tariffPlan = tariffPlanMapper.create(request);
            tariffPlan.setArchived(false);

            Long generatedId = tariffPlanService.createNewTariffPlan(tariffPlan);
            response.getWriter().write(generatedId.toString());
        } catch (UnableSaveEntityException e) {
            response.setStatus(e.getStatusCode());
            response.getWriter().write(e.getMessage());
        }
    }
}