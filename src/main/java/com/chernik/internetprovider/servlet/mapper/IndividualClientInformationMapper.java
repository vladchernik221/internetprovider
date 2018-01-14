package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;

import javax.servlet.http.HttpServletRequest;

@Component
public class IndividualClientInformationMapper extends Mapper<IndividualClientInformation> {
    @Override
    public IndividualClientInformation create(HttpServletRequest request) throws BadRequestException {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setFirstName(getMandatoryString(request.getParameter("individual.firstName")));
        individualClientInformation.setSecondName(getMandatoryString(request.getParameter("individual.secondName")));
        individualClientInformation.setLastName(getMandatoryString(request.getParameter("individual.lastName")));
        individualClientInformation.setAddress(getMandatoryString(request.getParameter("individual.address")));
        individualClientInformation.setPassportUniqueIdentification(getMandatoryString(request.getParameter("individual.passportUniqueIdentification")));
        individualClientInformation.setPhoneNumber(getNotMandatoryString(request.getParameter("individual.phoneNumber")));
        return individualClientInformation;
    }
}
