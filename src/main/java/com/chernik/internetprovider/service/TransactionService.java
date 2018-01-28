package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Transaction;

public interface TransactionService {
    void create(Transaction transaction) throws DatabaseException, TimeOutException, EntityNotFoundException;

    Page<Transaction> getPage(Long contractAnnexId, Pageable pageable) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
