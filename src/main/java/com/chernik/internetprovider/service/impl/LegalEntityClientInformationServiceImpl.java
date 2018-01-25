package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.repository.LegalEntityClientInformationRepository;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;

import java.util.Optional;

@Service
public class LegalEntityClientInformationServiceImpl implements LegalEntityClientInformationService {

    @Autowired
    private LegalEntityClientInformationRepository legalEntityClientInformationRepository;

    public void setLegalEntityClientInformationRepository(LegalEntityClientInformationRepository legalEntityClientInformationRepository) {
        this.legalEntityClientInformationRepository = legalEntityClientInformationRepository;
    }


    @Override
    public Long createOrUpdate(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException {
        Optional<LegalEntityClientInformation> checkingInformation = legalEntityClientInformationRepository.getByPayerAccountNumber(legalEntityClientInformation.getPayerAccountNumber());

        if (!checkingInformation.isPresent()) {
            return legalEntityClientInformationRepository.create(legalEntityClientInformation);
        } else {
            legalEntityClientInformationRepository.update(legalEntityClientInformation);
            return checkingInformation.get().getLegalEntityClientInformationId();
        }
    }

    @Override
    public LegalEntityClientInformation getByPayerAccountNumber(String payerAccountNumber) throws DatabaseException, TimeOutException {
        return legalEntityClientInformationRepository.getByPayerAccountNumber(payerAccountNumber).orElse(null);
    }

    @Override
    public LegalEntityClientInformation getById(Long id) throws DatabaseException, TimeOutException {
        return legalEntityClientInformationRepository.getById(id).orElse(null);
    }
}
