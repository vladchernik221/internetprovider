package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Account;

public interface AccountService {
    Account getById(Long contractAnnexId, Integer pageNumber) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
