package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;

public interface ContractAnnexHasServiceRepository {
    void create(Long contractAnnexId, Long serviceId) throws DatabaseException, TimeOutException;
}
