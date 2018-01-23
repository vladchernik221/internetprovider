package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.repository.TransactionRepository;
import com.chernik.internetprovider.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Long create(Transaction transaction) throws DatabaseException, TimeOutException {
        return transactionRepository.create(transaction);//TODO check contract annex id to exists
    }

    @Override
    public Page<Transaction> getPage(Long contractAnnexId, Pageable pageable) throws DatabaseException, TimeOutException {
        return transactionRepository.getPage(contractAnnexId, pageable);
    }
}
