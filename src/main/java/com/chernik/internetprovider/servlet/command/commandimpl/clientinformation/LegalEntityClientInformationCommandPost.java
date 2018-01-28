package com.chernik.internetprovider.servlet.command.commandimpl.clientinformation;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/client/legal", method = RequestType.POST)
public class LegalEntityClientInformationCommandPost implements Command {

    @Autowired
    private LegalEntityClientInformationService legalEntityClientInformationService;

    @Autowired
    private BaseMapper baseMapper;

    public void setLegalEntityClientInformationService(LegalEntityClientInformationService legalEntityClientInformationService) {
        this.legalEntityClientInformationService = legalEntityClientInformationService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String payerAccountNumber = baseMapper.getMandatoryString(request, "identifier");
        LegalEntityClientInformation legalEntityClientInformation = legalEntityClientInformationService.getByPayerAccountNumber(payerAccountNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseMessage = objectMapper.writeValueAsString(legalEntityClientInformation);

        response.getWriter().write(responseMessage);
    }
}
