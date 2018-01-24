package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Transaction;

public interface TransactionRepository {
    void create(Transaction transaction) throws DatabaseException, TimeOutException;

    Page<Transaction> getPage(Long contractAnnexId, Pageable pageable) throws DatabaseException, TimeOutException;
}
