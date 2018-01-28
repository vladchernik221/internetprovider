package com.chernik.internetprovider.servlet.command.impl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
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

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public void setContractMapper(ContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Contract contract = contractMapper.create(request);
        String userPassword = contractMapper.getMandatoryString(request, "password");

        Long generatedId = contractService.create(contract, userPassword);
        response.getWriter().write(generatedId.toString());
    }
}
