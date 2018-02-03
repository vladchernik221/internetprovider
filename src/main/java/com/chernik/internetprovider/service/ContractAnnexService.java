package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.User;

public interface ContractAnnexService {
    Long create(ContractAnnex contractAnnex, User user) throws BaseException;

    Page<ContractAnnex> getPage(Long contractId, Pageable pageable) throws BaseException;

    ContractAnnex getById(Long id, User user) throws BaseException;

    void cancel(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException, UnableSaveEntityException;

    boolean existById(Long id) throws DatabaseException, TimeOutException;
}
