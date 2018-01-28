package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;

import javax.servlet.http.HttpServletRequest;

@Component
public class LegalEntityClientInformationMapper extends BaseMapper implements Mapper<LegalEntityClientInformation> {
    @Override
    public LegalEntityClientInformation create(HttpServletRequest request) throws BadRequestException {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setName(getMandatoryString(request, "legal.name"));
        legalEntityClientInformation.setPayerAccountNumber(getMandatoryString(request, "legal.payerAccountNumber"));
        legalEntityClientInformation.setCheckingAccount(getMandatoryString(request, "legal.checkingAccount"));
        legalEntityClientInformation.setAddress(getMandatoryString(request, "legal.address"));
        legalEntityClientInformation.setPhoneNumber(getMandatoryString(request, "legal.phoneNumber"));
        return legalEntityClientInformation;
    }
}
