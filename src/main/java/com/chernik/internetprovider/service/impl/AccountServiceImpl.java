package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.AccessDeniedException;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
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

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @Override
    public Account getById(Long contractAnnexId, Integer pageNumber, User user) throws DatabaseException, TimeOutException, EntityNotFoundException, AccessDeniedException {
        if (user.getUserRole() == UserRole.ADMIN) {
            throw new AccessDeniedException("Access denied");
        } else if (user.getUserRole() == UserRole.CUSTOMER && !accountRepository.isUserOwner(contractAnnexId, user.getUserId())) {
            throw new EntityNotFoundException(String.format("Contract with id %s does not exist", contractAnnexId));
        }

        Optional<Account> accountOptional = accountRepository.getById(contractAnnexId);
        if (!accountOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Account with id: %d not found", contractAnnexId));
        }


        Page<Transaction> transactionPage = transactionService.getPage(contractAnnexId, new Pageable(pageNumber, 10));
        Account account = accountOptional.get();
        account.setTransactions(transactionPage);
        return account;
    }

    @Override
    public void addUsedTraffic(Long contractAnnexId, Integer usedTraffic) throws DatabaseException, TimeOutException, EntityNotFoundException {
        if (!accountRepository.existWithId(contractAnnexId)) {
            throw new EntityNotFoundException(String.format("Account with id=%d noes not exist", contractAnnexId));
        }

        accountRepository.addUsedTraffic(contractAnnexId, usedTraffic);
    }
}
