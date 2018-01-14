package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;

public interface LegalEntityClientInformationRepository {
    Long create(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException;

    boolean existsByPayerAccountNumber(String payerAccountNumber) throws DatabaseException, TimeOutException;
}