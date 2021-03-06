package com.chernik.internetprovider.servlet.command.impl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@HttpRequestProcessor(uri = "/contract/{\\d+}/dissolve", method = RequestType.POST)
public class ContractDissolveCommandPost implements Command {

    @Autowired
    private ContractService contractService;

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);
        contractService.dissolve(id);
    }
}
