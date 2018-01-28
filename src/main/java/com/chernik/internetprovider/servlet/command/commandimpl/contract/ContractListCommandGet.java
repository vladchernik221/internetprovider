package com.chernik.internetprovider.servlet.command.commandimpl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract", method = RequestType.GET)
public class ContractListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ContractListCommandGet.class);

    private static final String CONTRACT_LIST_PAGE = "/WEB-INF/jsp/contract/contractList.jsp";

    @Autowired
    private ContractService contractService;

    @Autowired
    private BaseMapper baseMapper;

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, BaseException, IOException {
        Long contractId = baseMapper.getNotMandatoryLong(request, "number");
        if (contractId != null) {
            Contract contract = contractService.getById(contractId);
            request.setAttribute("contract", contract);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTRACT_LIST_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", CONTRACT_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
