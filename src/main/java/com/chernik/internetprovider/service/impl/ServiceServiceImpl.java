package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.ServiceService;

import java.util.Optional;

@com.chernik.internetprovider.context.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ContractAnnexService contractAnnexService;

    public void setServiceRepository(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void setContractAnnexService(ContractAnnexService contractAnnexService) {
        this.contractAnnexService = contractAnnexService;
    }


    @Override
    public Long create(Service service) throws BaseException {
        if (serviceRepository.existWithName(service.getName())) {
            throw new UnableSaveEntityException(String.format("Service with name: %s already exist", service.getName()));
        }

        return serviceRepository.create(service);
    }

    @Override
    public void update(Service service) throws BaseException {
        if (!existWithId(service.getServiceId())) {
            throw new EntityNotFoundException(String.format("Service with id: %d does not exist", service.getServiceId()));
        }
        if (serviceRepository.existWithNameAndNotId(service.getServiceId(), service.getName())) {
            throw new UnableSaveEntityException(String.format("Service with name: %s already exists", service.getName()));
        }

        serviceRepository.update(service);
    }

    @Override
    public Page<Service> getPage(Pageable pageable, Boolean archived) throws BaseException {
        return serviceRepository.getPage(archived, pageable);
    }

    @Override
    public Service getById(Long id) throws BaseException {
        Optional<Service> service = serviceRepository.getById(id);
        if (!service.isPresent()) {
            throw new EntityNotFoundException(String.format("Service with id: %d does not exist", id));
        }

        return service.get();
    }

    @Override
    public void archive(Long id) throws BaseException {
        if (!existWithId(id)) {
            throw new EntityNotFoundException(String.format("Service with id: %d does not exist", id));
        }

        serviceRepository.archive(id);
    }

    @Override
    public Page<Service> getPageByContractAnnexId(Long id, Pageable pageable) throws EntityNotFoundException, DatabaseException, TimeOutException {
        if (!contractAnnexService.existById(id)) {
            throw new EntityNotFoundException(String.format("Service with id: %d does not exist", id));
        }

        return serviceRepository.getPageByContractAnnexId(id, pageable);
    }

    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        return serviceRepository.existWithId(id);
    }
}
