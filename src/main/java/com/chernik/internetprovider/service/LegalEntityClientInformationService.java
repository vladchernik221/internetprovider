package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;

public interface LegalEntityClientInformationService {
    Long createOrUpdate(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException;

    LegalEntityClientInformation getByPayerAccountNumber(String payerAccountNumber) throws DatabaseException, TimeOutException;
}
