package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;

public interface ContractAnnexRepository {
    Long create(ContractAnnex contractAnnex) throws DatabaseException, TimeOutException;
}