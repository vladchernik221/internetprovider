package com.chernik.internetprovider.servlet.command.commandimpl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.ContractMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/new", method = RequestType.POST)
public class ContractCreateCommandPost implements Command {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Contract contract = contractMapper.create(request);
        String userPassword = request.getParameter("password");
        if (userPassword == null || userPassword.isEmpty()) {
            throw new BadRequestException("Mandatory parameter password does not initialize");
        }

        Long generatedId = contractService.create(contract, userPassword);
        response.getWriter().write(generatedId.toString());
    }
}
