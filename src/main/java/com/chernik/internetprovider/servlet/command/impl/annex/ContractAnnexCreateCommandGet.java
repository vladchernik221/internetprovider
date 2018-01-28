package com.chernik.internetprovider.servlet.command.impl.annex;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@HttpRequestProcessor(uri = "/contract/{\\d+}/annex/new", method = RequestType.GET)
public class ContractAnnexCreateCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ContractAnnexCreateCommandGet.class);

    private static final String ANNEX_FORM_PAGE = "/WEB-INF/jsp/annex/annexForm.jsp";

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private ContractService contractService;

    public void setTariffPlanService(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DatabaseException, TimeOutException, EntityNotFoundException {
        String contractId = request.getRequestURI().split("/")[2];
        if (!contractService.existById(Long.valueOf(contractId))) {
            throw new EntityNotFoundException(String.format("Contract with id=%s not found", contractId));
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(ANNEX_FORM_PAGE);

        List<TariffPlan> tariffPlans = tariffPlanService.getAllNotArchived();
        request.setAttribute("tariffPlans", tariffPlans);
        request.setAttribute("contractId", contractId);

        LOGGER.log(Level.TRACE, "Forward to page: {}", ANNEX_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
