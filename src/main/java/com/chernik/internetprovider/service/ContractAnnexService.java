package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;

public interface ContractAnnexService {
    Long create(ContractAnnex contractAnnex) throws BaseException;
}
