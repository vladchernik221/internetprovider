package com.chernik.internetprovider.servlet.command.commandimpl.annex;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/annex/{\\d+}", method = RequestType.GET)
public class ContractAnnexByIdCommandGet implements Command {
    private static final String ANNEX_PAGE = "/WEB-INF/jsp/annex/annex.jsp";

//    @Autowired
//    private ContractService contractService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String pathParameter = request.getRequestURI().split("/")[2];
        Long id = Long.valueOf(pathParameter);
//        ContractAnnex contractAnnex = contractAnnexService.getById(id);

//        request.setAttribute("annex", contractAnnex);
        RequestDispatcher dispatcher = request.getRequestDispatcher(ANNEX_PAGE);
        dispatcher.forward(request, response);
    }
}
