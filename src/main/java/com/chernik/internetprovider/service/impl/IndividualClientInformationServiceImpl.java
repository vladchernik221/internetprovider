package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.repository.IndividualClientInformationRepository;
import com.chernik.internetprovider.service.IndividualClientInformationService;

import java.util.Optional;

@Service
public class IndividualClientInformationServiceImpl implements IndividualClientInformationService {

    @Autowired
    private IndividualClientInformationRepository individualClientInformationRepository;

    @Override
    public Long createOrUpdate(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException {
        Optional<IndividualClientInformation> checkingInformation = individualClientInformationRepository.getByPassportData(individualClientInformation.getPassportUniqueIdentification());

        if (!checkingInformation.isPresent()) {
            return individualClientInformationRepository.create(individualClientInformation);
        } else {
            individualClientInformationRepository.update(individualClientInformation);
            return checkingInformation.get().getIndividualClientInformationId();
        }
    }

    @Override
    public IndividualClientInformation getByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException {
        return individualClientInformationRepository.getByPassportData(passportUniqueIdentification).orElse(null);
    }

    @Override
    public IndividualClientInformation getById(Long id) throws DatabaseException, TimeOutException {
        return individualClientInformationRepository.getById(id).orElse(null);
    }
}
