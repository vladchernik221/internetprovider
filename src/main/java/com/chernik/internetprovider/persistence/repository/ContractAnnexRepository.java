package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;

import java.util.Optional;

public interface ContractAnnexRepository {
    Long create(ContractAnnex contractAnnex) throws DatabaseException, TimeOutException;

    Page<ContractAnnex> getPage(Long contractId, Pageable pageable) throws DatabaseException, TimeOutException;

    Optional<ContractAnnex> getById(Long id) throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;

    void cancel(Long id) throws DatabaseException, TimeOutException;

    boolean isCanceled(Long id) throws DatabaseException, TimeOutException;
}
