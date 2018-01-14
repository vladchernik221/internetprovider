package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;

import java.util.Optional;

public interface ContractService {
    Long create(Contract contract) throws DatabaseException, TimeOutException;

    Contract getById(Long id) throws DatabaseException, TimeOutException;

    Optional<Contract> getByClientInformation(Contract contract) throws DatabaseException, TimeOutException;
}
