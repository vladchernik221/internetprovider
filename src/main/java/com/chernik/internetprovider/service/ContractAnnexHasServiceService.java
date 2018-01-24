package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;

public interface ContractAnnexHasServiceService {
    void create(Long contractAnnexId, Long serviceId) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
