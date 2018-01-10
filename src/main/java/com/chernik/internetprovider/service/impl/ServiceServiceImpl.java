package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import com.chernik.internetprovider.service.ServiceService;

import java.util.Optional;

@com.chernik.internetprovider.context.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Long create(Service service) throws BaseException {
        if (!serviceRepository.existWithName(service.getName())) {
            return serviceRepository.create(service);
        } else {
            throw new UnableSaveEntityException(String.format("Service with name: %s already exist", service.getName()));
        }
    }

    @Override
    public void update(Service service) throws BaseException {
        if (serviceRepository.existWithId(service.getServiceId())) {
            serviceRepository.update(service);
        } else {
            throw new EntityNotFoundException(String.format("Service with id: %s does not exist", service.getServiceId()));
        }
    }

    @Override
    public Page<Service> getPage(Pageable pageable, Boolean archived) throws BaseException {
        return serviceRepository.getPage(archived, pageable);
    }

    @Override
    public Service getById(Long id) throws BaseException {
        Optional<Service> service = serviceRepository.getById(id);
        if (!service.isPresent()) {
            throw new EntityNotFoundException(String.format("Service with id=%d does not exist", id));
        }
        return service.get();
    }

    @Override
    public void archive(Long id) throws BaseException {
        Service service = getById(id);
        service.setArchived(!service.getArchived());
        serviceRepository.archive(service);
    }
}
