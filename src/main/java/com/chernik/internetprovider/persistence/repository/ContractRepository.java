package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;

import java.util.Optional;

public interface ContractRepository {
    Long create(Contract contract) throws DatabaseException, TimeOutException;

    void dissolve(Long id) throws DatabaseException, TimeOutException;

    Optional<Contract> getById(Long id) throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;

    boolean existNotDissolvedByClientInformation(Contract contract) throws DatabaseException, TimeOutException;

    boolean isDissolved(Long contractId) throws DatabaseException, TimeOutException;

    boolean hasNotCanceledContractAnnex(Long contractId) throws DatabaseException, TimeOutException;
}
