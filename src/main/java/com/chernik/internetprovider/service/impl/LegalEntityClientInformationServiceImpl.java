package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.repository.LegalEntityClientInformationRepository;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;

@Service
public class LegalEntityClientInformationServiceImpl implements LegalEntityClientInformationService {

    @Autowired
    private LegalEntityClientInformationRepository legalEntityClientInformationRepository;

    @Override
    public Long create(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (!legalEntityClientInformationRepository.existsByPayerAccountNumber(legalEntityClientInformation.getPayerAccountNumber())) {
            return legalEntityClientInformationRepository.create(legalEntityClientInformation);
        } else {
            throw new UnableSaveEntityException(String.format("Legal entity with payer number: %s already exists", legalEntityClientInformation.getPayerAccountNumber()));
        }
    }

    @Override
    public LegalEntityClientInformation getByPayerAccountNumber(String payerAccountNumber) throws DatabaseException, TimeOutException {
        return legalEntityClientInformationRepository.getByPayerAccountNumber(payerAccountNumber).get();
    }
}
