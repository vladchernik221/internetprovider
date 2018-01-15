package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import com.chernik.internetprovider.service.ContractService;

import com.chernik.internetprovider.service.IndividualClientInformationService;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private IndividualClientInformationService individualClientInformationService;

    @Autowired
    private LegalEntityClientInformationService legalEntityClientInformationService;

    @Override
    public Long create(Contract contract) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (contractRepository.existNotDissolvedByClientInformation(contract)) {
            throw new UnableSaveEntityException("Current client already has not dissolved contract");
        }

        Long clientInformationId;
        switch (contract.getClientType()) {
            case INDIVIDUAL:
                IndividualClientInformation individualClientInformation = contract.getIndividualClientInformation();
                clientInformationId = individualClientInformationService.createOrUpdate(individualClientInformation);
                individualClientInformation.setIndividualClientInformationId(clientInformationId);
                break;
            case LEGAL:
                LegalEntityClientInformation legalEntityClientInformation = contract.getLegalEntityClientInformation();
                clientInformationId = legalEntityClientInformationService.createOrUpdate(legalEntityClientInformation);
                legalEntityClientInformation.setLegalEntityClientInformationId(clientInformationId);
                break;
        }
        return contractRepository.create(contract);
    }

    @Override
    public Contract getById(Long id) throws DatabaseException, TimeOutException {
        Contract contract = contractRepository.getById(id).orElse(null);
        if (contract != null) {
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
        }
        return contract;
    }

    @Override
    public Contract getByIdOrThrow(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Contract contract = getById(id);
        if (contract == null) {
            throw new EntityNotFoundException(String.format("Contract with id %s does not exist", id));
        }
        return contract;
    }

    @Override
    public void dissolve(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        if (!contractRepository.existWithId(id)) {
            throw new EntityNotFoundException(String.format("Contract with ID %d was not found", id));
        }
        contractRepository.dissolve(id);
    }
}
