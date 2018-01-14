package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;

public interface ContractService {
    Long create(Contract contract) throws DatabaseException, TimeOutException;
}
