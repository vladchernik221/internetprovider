package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.repository.ContractAnnexRepository;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import com.chernik.internetprovider.service.ContractAnnexService;

import java.util.Optional;

@Service
public class ContractAnnexServiceImpl implements ContractAnnexService {

    @Autowired
    private ContractAnnexRepository contractAnnexRepository;

    @Autowired
    private TariffPlanRepository tariffPlanRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public Long create(ContractAnnex contractAnnex) throws BaseException {
        if (!tariffPlanRepository.existWithId(contractAnnex.getTariffPlan().getTariffPlanId())) {
            throw new EntityNotFoundException(String.format("Tariff plan with ID %d was not found.", contractAnnex.getTariffPlan().getTariffPlanId()));
        }
        Optional<Contract> contract = contractRepository.getById(contractAnnex.getContract().getContractId());
        if (!contract.isPresent()) {
            throw new EntityNotFoundException(String.format("Contract with ID %d was not found.", contractAnnex.getContract().getContractId()));
        }
        if (contract.get().getDissolved()) {
            throw new UnableSaveEntityException(String.format("Contract with ID %d already dissolved. It's impossible to add annex to dissolved contract.", contractAnnex.getContract().getContractId()));
        }
        return contractAnnexRepository.create(contractAnnex);
    }
}
