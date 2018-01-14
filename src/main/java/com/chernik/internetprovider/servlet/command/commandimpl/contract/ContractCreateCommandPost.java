package com.chernik.internetprovider.servlet.command.commandimpl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.service.ContractIndividualLegalService;
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
    private ContractIndividualLegalService contractIndividualLegalService;

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Contract contract = contractMapper.create(request);
        Long generatedId = contractIndividualLegalService.create(contract);
        response.getWriter().write(generatedId.toString());
    }
}
