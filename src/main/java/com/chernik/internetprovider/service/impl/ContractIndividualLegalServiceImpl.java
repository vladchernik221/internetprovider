package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.service.ContractIndividualLegalService;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.IndividualClientInformationService;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;

@Service
public class ContractIndividualLegalServiceImpl implements ContractIndividualLegalService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private IndividualClientInformationService individualClientInformationService;

    @Autowired
    private LegalEntityClientInformationService legalEntityClientInformationService;

    @Override
    public Long create(Contract contract) throws TimeOutException, DatabaseException {
        Long id;
        switch (contract.getClientType()) {
            case INDIVIDUAL:
                IndividualClientInformation individualClientInformation = contract.getIndividualClientInformation();
                id = individualClientInformationService.createOrUpdate(individualClientInformation);
                individualClientInformation.setIndividualClientInformationId(id);
                break;
            case LEGAL:
                LegalEntityClientInformation legalEntityClientInformation = contract.getLegalEntityClientInformation();
                id = legalEntityClientInformationService.createOrUpdate(legalEntityClientInformation);
                legalEntityClientInformation.setLegalEntityClientInformationId(id);
                break;
        }
        return contractService.create(contract);
    }

    @Override
    public Contract getById(Long id) {
        Contract contract = contractService.getById(id);
        switch (contract.getClientType()) {
            case INDIVIDUAL:
                IndividualClientInformation individualClientInformation = individualClientInformationService.getById(contract.getIndividualClientInformation().getIndividualClientInformationId());
                contract.setIndividualClientInformation(individualClientInformation);
                break;
            case LEGAL:
                LegalEntityClientInformation legalEntityClientInformation = legalEntityClientInformationService.getById(contract.getLegalEntityClientInformation().getLegalEntityClientInformationId());
                contract.setLegalEntityClientInformation(legalEntityClientInformation);
                break;
        }
        return contract;
    }
}
