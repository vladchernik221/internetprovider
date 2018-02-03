package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.User;

public interface ContractService {
    Long create(Contract contract, String userPassword) throws DatabaseException, TimeOutException, UnableSaveEntityException;

    Contract getById(Long id) throws DatabaseException, TimeOutException;

    Contract getByIdOrThrow(Long id, User user) throws DatabaseException, TimeOutException, EntityNotFoundException, AccessDeniedException;

    void dissolve(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException, UnableSaveEntityException;

    boolean existById(Long id) throws DatabaseException, TimeOutException;
}
