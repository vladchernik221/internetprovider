package com.chernik.internetprovider.servlet.command.commandimpl.clientinformation;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.service.IndividualClientInformationService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/client/individual", method = RequestType.POST)
public class IndividualClientInformationCommandPost implements Command {

    @Autowired
    private IndividualClientInformationService individualClientInformationService;

    public void setIndividualClientInformationService(IndividualClientInformationService individualClientInformationService) {
        this.individualClientInformationService = individualClientInformationService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String passportUniqueIdentification = request.getParameter("identifier");
        if (passportUniqueIdentification != null) {
            IndividualClientInformation individualClientInformation = individualClientInformationService.getByPassportData(passportUniqueIdentification);

            ObjectMapper objectMapper = new ObjectMapper();
            String responseMessage = objectMapper.writeValueAsString(individualClientInformation);

            response.getWriter().write(responseMessage);
        } else {
            throw new BadRequestException("Missing parameter: identifier");
        }
    }
}
