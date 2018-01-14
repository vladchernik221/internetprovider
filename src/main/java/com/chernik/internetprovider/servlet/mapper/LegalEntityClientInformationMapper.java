package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;

import javax.servlet.http.HttpServletRequest;

@Component
public class LegalEntityClientInformationMapper extends Mapper<LegalEntityClientInformation> {
    @Override
    public LegalEntityClientInformation create(HttpServletRequest request) throws BadRequestException {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setName(getMandatoryString(request.getParameter("name")));
        legalEntityClientInformation.setPayerAccountNumber(getMandatoryString(request.getParameter("payerAccountNumber")));
        legalEntityClientInformation.setAddress(getMandatoryString(request.getParameter("address")));
        legalEntityClientInformation.setPhoneNumber(getMandatoryString("phoneNumber"));
        return legalEntityClientInformation;
    }
}
