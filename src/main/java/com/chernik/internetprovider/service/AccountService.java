package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.AccessDeniedException;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.User;

public interface AccountService {
    Account getById(Long contractAnnexId, Integer pageNumber, User user) throws DatabaseException, TimeOutException, EntityNotFoundException, AccessDeniedException;

    void addUsedTraffic(Long accountId, Integer usedTraffic) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
