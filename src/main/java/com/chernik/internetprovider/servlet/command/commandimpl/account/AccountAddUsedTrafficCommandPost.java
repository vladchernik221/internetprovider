package com.chernik.internetprovider.servlet.command.commandimpl.account;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/traffic", method = RequestType.POST)
public class AccountAddUsedTrafficCommandPost implements Command {

    @Autowired
    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Long contractAnnexId = Long.valueOf(request.getParameter("contractAnnexId"));
        Integer usedTraffic = Integer.valueOf(request.getParameter("usedTraffic"));

        accountService.addUsedTraffic(contractAnnexId, usedTraffic);
    }
}
