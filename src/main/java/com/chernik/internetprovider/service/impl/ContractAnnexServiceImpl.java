package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.ContractAnnexRepository;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.TariffPlanService;

import java.util.Optional;

@Service
public class ContractAnnexServiceImpl implements ContractAnnexService {

    @Autowired
    private ContractAnnexRepository contractAnnexRepository;

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private ContractService contractService;

    public void setContractAnnexRepository(ContractAnnexRepository contractAnnexRepository) {
        this.contractAnnexRepository = contractAnnexRepository;
    }

    public void setTariffPlanService(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }


    @Override
    public Long create(ContractAnnex contractAnnex) throws BaseException {
        if (!tariffPlanService.existWithId(contractAnnex.getTariffPlan().getTariffPlanId())) {
            throw new EntityNotFoundException(String.format("Tariff plan with ID %d was not found.", contractAnnex.getTariffPlan().getTariffPlanId()));
        }

        Contract contract = contractService.getByIdOrThrow(contractAnnex.getContract().getContractId());
        if (contract.getDissolved()) {
            throw new UnableSaveEntityException(String.format("Contract with ID %d already dissolved. It's impossible to add annex to dissolved contract.", contractAnnex.getContract().getContractId()));
        }

        return contractAnnexRepository.create(contractAnnex);
    }

    @Override
    public Page<ContractAnnex> getPage(Long contractId, Pageable pageable) throws BaseException {
        if (contractService.notExistById(contractId)) {
            throw new EntityNotFoundException(String.format("Contract with id=%d not found", contractId));
        }

        Page<ContractAnnex> contractAnnexPage = contractAnnexRepository.getPage(contractId, pageable);
        TariffPlan tariffPlan;
        for (ContractAnnex contractAnnex : contractAnnexPage.getData()) {
            tariffPlan = tariffPlanService.getById(contractAnnex.getTariffPlan().getTariffPlanId());
            contractAnnex.setTariffPlan(tariffPlan);
        }
        return contractAnnexPage;
    }

    @Override
    public ContractAnnex getById(Long id) throws BaseException {
        Optional<ContractAnnex> contractAnnex = contractAnnexRepository.getById(id);
        if (!contractAnnex.isPresent()) {
            throw new EntityNotFoundException(String.format("Contract annex with id=%d does not exist", id));
        }
        TariffPlan tariffPlan = tariffPlanService.getById(contractAnnex.get().getTariffPlan().getTariffPlanId());
        contractAnnex.get().setTariffPlan(tariffPlan);
        return contractAnnex.get();
    }

    @Override
    public void cancel(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException, UnableSaveEntityException {
        if (!existById(id)) {
            throw new EntityNotFoundException(String.format("Contract annex with id=%d does not exist", id));
        }
        if (contractAnnexRepository.isCanceled(id)) {
            throw new UnableSaveEntityException(String.format("Contract annex with id=%d already canceled", id));
        }

        contractAnnexRepository.cancel(id);
    }

    @Override
    public boolean existById(Long id) throws DatabaseException, TimeOutException {
        return contractAnnexRepository.existWithId(id);
    }
}
