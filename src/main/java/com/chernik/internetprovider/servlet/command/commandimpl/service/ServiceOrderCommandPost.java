package com.chernik.internetprovider.servlet.command.commandimpl.service;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.ContractAnnexHasServiceService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/annex/{\\d+}/service", method = RequestType.POST)
public class ServiceOrderCommandPost implements Command {

    @Autowired
    private ContractAnnexHasServiceService contractAnnexHasServiceService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Long contractAnnexId = Long.valueOf(request.getRequestURI().split("/")[3]);
        Long serviceId = Long.valueOf(request.getParameter("serviceId"));

        contractAnnexHasServiceService.create(contractAnnexId, serviceId);
    }
}
