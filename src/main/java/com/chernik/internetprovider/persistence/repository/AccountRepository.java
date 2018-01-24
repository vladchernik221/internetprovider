package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getById(Long contractAnnexId) throws DatabaseException, TimeOutException;

    void addUsedTraffic(Long contractAnnexId, Integer usedTraffic) throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;
}
