package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.repository.ContractAnnexHasServiceRepository;
import com.chernik.internetprovider.service.ContractAnnexHasServiceService;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.ServiceService;

@Service
public class ContractAnnexHasServiceServiceImpl implements ContractAnnexHasServiceService {

    @Autowired
    private ContractAnnexHasServiceRepository contractAnnexHasServiceRepository;

    @Autowired
    private ContractAnnexService contractAnnexService;

    @Autowired
    private ServiceService serviceService;

    public void setContractAnnexHasServiceRepository(ContractAnnexHasServiceRepository contractAnnexHasServiceRepository) {
        this.contractAnnexHasServiceRepository = contractAnnexHasServiceRepository;
    }

    public void setContractAnnexService(ContractAnnexService contractAnnexService) {
        this.contractAnnexService = contractAnnexService;
    }

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }


    @Override
    public void create(Long contractAnnexId, Long serviceId) throws DatabaseException, TimeOutException, EntityNotFoundException {
        if (!contractAnnexService.existById(contractAnnexId)) {
            throw new EntityNotFoundException(String.format("Contract annex with id=%d does not exist", contractAnnexId));
        }
        if (!serviceService.existWithId(serviceId)) {
            throw new EntityNotFoundException(String.format("Service with id=%d does not exist", serviceId));
        }

        contractAnnexHasServiceRepository.create(contractAnnexId, serviceId);
    }
}
