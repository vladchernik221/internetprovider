package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;

import java.util.Optional;

public interface IndividualClientInformationRepository {
    Long create(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException;

    boolean existByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException;

    Optional<IndividualClientInformation> getByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException;

    void update(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException;

    Optional<IndividualClientInformation> getById(Long id) throws DatabaseException, TimeOutException;
}
