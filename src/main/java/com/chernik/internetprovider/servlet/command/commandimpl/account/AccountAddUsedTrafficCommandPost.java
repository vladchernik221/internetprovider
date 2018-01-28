package com.chernik.internetprovider.servlet.command.commandimpl.account;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/traffic", method = RequestType.POST)
public class AccountAddUsedTrafficCommandPost implements Command {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BaseMapper baseMapper;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Long contractAnnexId = baseMapper.getMandatoryLong(request, "contractAnnexId");
        Integer usedTraffic = baseMapper.getMandatoryInt(request, "usedTraffic");

        accountService.addUsedTraffic(contractAnnexId, usedTraffic);
    }
}
