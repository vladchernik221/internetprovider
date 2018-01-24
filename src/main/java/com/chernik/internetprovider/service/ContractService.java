package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.Contract;

public interface ContractService {
    Long create(Contract contract, String userPassword) throws DatabaseException, TimeOutException, UnableSaveEntityException;

    Contract getById(Long id) throws DatabaseException, TimeOutException;

    Contract getByIdOrThrow(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException;

    void dissolve(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException, UnableSaveEntityException;

    boolean notExistById(Long id) throws DatabaseException, TimeOutException;
}
