package com.chernik.internetprovider.servlet.command.commandimpl.contract;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/{\\d+}", method = RequestType.GET)
public class ContractByIdCommandGet implements Command {
    private static final String CONTRACT_PAGE = "/WEB-INF/jsp/contract/contract.jsp";

//    @Autowired
//    private ContractService contractService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
//        Contract contract = contractService.getById(id);

//        request.setAttribute("contract", contract);
        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTRACT_PAGE);
        dispatcher.forward(request, response);
    }
}
