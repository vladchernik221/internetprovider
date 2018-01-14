package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import com.chernik.internetprovider.service.ContractService;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public Long create(Contract contract) throws DatabaseException, TimeOutException {
        return contractRepository.create(contract);
    }

    @Override
    public Contract getById(Long id) throws DatabaseException, TimeOutException {
        return contractRepository.getById(id).orElse(null);
    }
}
