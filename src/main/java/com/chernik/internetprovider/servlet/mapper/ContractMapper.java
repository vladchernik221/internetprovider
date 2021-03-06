package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.ClientType;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;

import javax.servlet.http.HttpServletRequest;

@Component
public class ContractMapper extends BaseMapper implements Mapper<Contract> {

    @Autowired
    private IndividualClientInformationMapper individualClientInformationMapper;

    @Autowired
    private LegalEntityClientInformationMapper legalEntityClientInformationMapper;

    public void setIndividualClientInformationMapper(IndividualClientInformationMapper individualClientInformationMapper) {
        this.individualClientInformationMapper = individualClientInformationMapper;
    }

    public void setLegalEntityClientInformationMapper(LegalEntityClientInformationMapper legalEntityClientInformationMapper) {
        this.legalEntityClientInformationMapper = legalEntityClientInformationMapper;
    }


    @Override
    public Contract create(HttpServletRequest request) throws BadRequestException {
        Contract contract = new Contract();
        ClientType clientType = ClientType.valueOf(getMandatoryString(request, "clientType").toUpperCase());
        contract.setClientType(clientType);
        switch (clientType) {
            case INDIVIDUAL:
                IndividualClientInformation individualClientInformation = individualClientInformationMapper.create(request);
                contract.setIndividualClientInformation(individualClientInformation);
                break;
            case LEGAL:
                LegalEntityClientInformation legalEntityClientInformation = legalEntityClientInformationMapper.create(request);
                contract.setLegalEntityClientInformation(legalEntityClientInformation);
                break;
        }
        return contract;
    }
}
