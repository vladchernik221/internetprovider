package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.repository.AccountRepository;
import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.service.TransactionService;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public Account getById(Long contractAnnexId, Integer pageNumber) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Optional<Account> accountOptional = accountRepository.getById(contractAnnexId);

        if (accountOptional.isPresent()) {
            Page<Transaction> transactionPage = transactionService.getPage(contractAnnexId, new Pageable(pageNumber, 10));
            Account account = accountOptional.get();
            account.setTransactions(transactionPage);
            return account;
        } else {
            throw new EntityNotFoundException(String.format("Account with id: %d not found", contractAnnexId));
        }
    }
}
