package com.chernik.internetprovider.servlet.command.impl.account;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/annex/{\\d+}/account", method = RequestType.GET)
public class AccountByIdCommandGet implements Command {

    private static final String ACCOUNT_PAGE = "/WEB-INF/jsp/account/account.jsp";

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Long contractAnnexId = Long.valueOf(request.getRequestURI().split("/")[3]);

        Integer pageNumber = baseMapper.getNotMandatoryInt(request, "page");
        pageNumber = (pageNumber != null) ? pageNumber - 1 : 0;

        Account account = accountService.getById(contractAnnexId, pageNumber, user);

        request.setAttribute("account", account);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ACCOUNT_PAGE);
        dispatcher.forward(request, response);
    }
}
