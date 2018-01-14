package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;

public interface IndividualClientInformationService {
    Long create(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException, UnableSaveEntityException;

    IndividualClientInformation getByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException;
}
