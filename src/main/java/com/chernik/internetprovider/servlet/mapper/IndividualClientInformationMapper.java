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
        individualClientInformation.setFirstName(getMandatoryString(request.getParameter("firstName")));
        individualClientInformation.setSecondName(getMandatoryString(request.getParameter("secondName")));
        individualClientInformation.setLastName(getMandatoryString(request.getParameter("lastName")));
        individualClientInformation.setAddress(getMandatoryString(request.getParameter("address")));
        individualClientInformation.setPassportUniqueIdentification(getMandatoryString(request.getParameter("passportUniqueIdentification")));
        individualClientInformation.setPhoneNumber(getNotMandatoryString(request.getParameter("phoneNumber")));
        return individualClientInformation;
    }
}
