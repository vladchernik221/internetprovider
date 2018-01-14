package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;

public interface ContractIndividualLegalService {
    Long create(Contract contract) throws TimeOutException, DatabaseException;

    Contract getById(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
