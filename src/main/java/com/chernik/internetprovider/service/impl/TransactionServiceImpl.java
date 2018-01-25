package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.repository.TransactionRepository;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ContractAnnexService contractAnnexService;

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setContractAnnexService(ContractAnnexService contractAnnexService) {
        this.contractAnnexService = contractAnnexService;
    }


    @Override
    public void create(Transaction transaction) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Long contractAnnexId = transaction.getAccount().getContractAnnex().getContractAnnexId();

        if (!contractAnnexService.existById(contractAnnexId)) {
            throw new EntityNotFoundException(String.format("Contract annex with id=%d does not exist", contractAnnexId));
        }

        transactionRepository.create(transaction);
    }

    @Override
    public Page<Transaction> getPage(Long contractAnnexId, Pageable pageable) throws DatabaseException, TimeOutException {
        return transactionRepository.getPage(contractAnnexId, pageable);
    }
}
