package com.chernik.internetprovider.servlet.command.commandimpl.annex;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.service.ContractAnnexService;
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

@HttpRequestProcessor(uri = "/contract/{\\d+}/annex", method = RequestType.GET)
public class ContractAnnexListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ContractAnnexListCommandGet.class);

    private static final String CONTRACT_ANNEX_LIST_PAGE = "/WEB-INF/jsp/annex/annexList.jsp";

    @Autowired
    private ContractAnnexService contractAnnexService;

    public void setContractAnnexService(ContractAnnexService contractAnnexService) {
        this.contractAnnexService = contractAnnexService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, BaseException, IOException {
        int pageNumber = 0;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Long contractId = Long.valueOf(request.getRequestURI().split("/")[2]);

        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTRACT_ANNEX_LIST_PAGE);
        Page<ContractAnnex> contractAnnexesPage = contractAnnexService.getPage(contractId, new Pageable(pageNumber, 10));//TODO to property or constant or somewhere
        request.setAttribute("contractAnnexesPage", contractAnnexesPage);
        request.setAttribute("contractId", contractId);
        LOGGER.log(Level.TRACE, "Forward to page: {}", CONTRACT_ANNEX_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
