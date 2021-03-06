package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;

public interface IndividualClientInformationService {
    Long save(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException;

    IndividualClientInformation getByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException;
    
    IndividualClientInformation getById(Long id) throws DatabaseException, TimeOutException;
}
