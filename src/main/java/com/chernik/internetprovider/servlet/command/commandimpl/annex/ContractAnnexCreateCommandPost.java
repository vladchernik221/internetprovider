package com.chernik.internetprovider.servlet.command.commandimpl.annex;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.ContractAnnexMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/{\\d+}/annex/new", method = RequestType.POST)
public class ContractAnnexCreateCommandPost implements Command {

    @Autowired
    private ContractAnnexService contractAnnexService;

    @Autowired
    private ContractAnnexMapper contractAnnexMapper;

    public void setContractAnnexService(ContractAnnexService contractAnnexService) {
        this.contractAnnexService = contractAnnexService;
    }

    public void setContractAnnexMapper(ContractAnnexMapper contractAnnexMapper) {
        this.contractAnnexMapper = contractAnnexMapper;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        ContractAnnex contractAnnex = contractAnnexMapper.create(request);
        Long generatedId = contractAnnexService.create(contractAnnex);
        response.getWriter().write(generatedId.toString());
    }
}
