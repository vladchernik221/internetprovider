package com.chernik.internetprovider.servlet.command.commandimpl.account;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/annex/{\\d+}/account", method = RequestType.GET)
public class AccountByIdCommandGet implements Command {

    private static final String ACCOUNT_PAGE = "/WEB-INF/jsp/account/account.jsp";

    @Autowired
    private AccountService accountService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {//TODO check exists
        Long contractAnnexId = Long.valueOf(request.getRequestURI().split("/")[3]);
        String page = request.getParameter("page");
        Integer pageNumber;
        if (page != null) {
            pageNumber = Integer.parseInt(page);
        } else {
            pageNumber = 1;
        }

        Account account = accountService.getById(contractAnnexId, pageNumber);

        request.setAttribute("account", account);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ACCOUNT_PAGE);
        dispatcher.forward(request, response);
    }
}
