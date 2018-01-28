package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;

import javax.servlet.http.HttpServletRequest;

@Component
public class IndividualClientInformationMapper extends BaseMapper implements Mapper<IndividualClientInformation> {
    @Override
    public IndividualClientInformation create(HttpServletRequest request) throws BadRequestException {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setFirstName(getMandatoryString(request, "individual.firstName"));
        individualClientInformation.setSecondName(getMandatoryString(request, "individual.secondName"));
        individualClientInformation.setLastName(getMandatoryString(request, "individual.lastName"));
        individualClientInformation.setAddress(getMandatoryString(request, "individual.address"));
        individualClientInformation.setPassportUniqueIdentification(getMandatoryString(request, "individual.passportUniqueIdentification"));
        individualClientInformation.setPhoneNumber(getNotMandatoryString(request, "individual.phoneNumber"));
        return individualClientInformation;
    }
}
