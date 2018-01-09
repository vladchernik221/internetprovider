package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;

public interface ServiceService {
    Long create(Service service) throws BaseException;

    void update(Service service) throws BaseException;

    Page<Service> getPage(Pageable pageable, Boolean archived) throws BaseException;

    Service getById(Long id) throws BaseException;

    void archive(Long id) throws BaseException;
}
