package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.repository.IndividualClientInformationRepository;
import com.chernik.internetprovider.service.IndividualClientInformationService;

@Service
public class IndividualClientInformationServiceImpl implements IndividualClientInformationService {

    @Autowired
    private IndividualClientInformationRepository individualClientInformationRepository;

    @Override
    public Long create(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (!individualClientInformationRepository.existByPassportData(individualClientInformation.getPassportUniqueIdentification())) {
            return individualClientInformationRepository.create(individualClientInformation);
        } else {
            throw new UnableSaveEntityException(String.format("Individual client with passport unique identification: %s already exist", individualClientInformation.getPassportUniqueIdentification()));
        }
    }

    @Override
    public IndividualClientInformation getByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException {
        return individualClientInformationRepository.getByPassportData(passportUniqueIdentification).get();
    }
}
