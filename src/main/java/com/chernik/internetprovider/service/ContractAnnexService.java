package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;

public interface ContractAnnexService {
    Long create(ContractAnnex contractAnnex) throws BaseException;

    Page<ContractAnnex> getPage(Long contractId, Pageable pageable) throws BaseException;

    ContractAnnex getById(Long id) throws BaseException;

    void cancel(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
