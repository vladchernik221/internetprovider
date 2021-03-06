package com.chernik.internetprovider.servlet.command.impl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/{\\d+}", method = RequestType.GET)
public class ContractByIdCommandGet implements Command {
    private static final String CONTRACT_PAGE = "/WEB-INF/jsp/contract/contract.jsp";

    @Autowired
    private ContractService contractService;

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
        Contract contract = contractService.getByIdOrThrow(id, user);

        request.setAttribute("contract", contract);
        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTRACT_PAGE);
        dispatcher.forward(request, response);
    }
}
