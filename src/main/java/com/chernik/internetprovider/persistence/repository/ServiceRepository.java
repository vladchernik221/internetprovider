package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;

import java.util.Optional;

public interface ServiceRepository {
    Long create(Service service) throws DatabaseException, TimeOutException;

    void update(Service service) throws DatabaseException, TimeOutException;

    void archive(Service service) throws DatabaseException, TimeOutException;

    Page<Service> getServicePage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException;

    Optional<Service> getById(Long id) throws DatabaseException, TimeOutException;

    boolean isServiceWithNameExist(String name) throws DatabaseException, TimeOutException;

    boolean isServiceWithIdExist(Long id) throws DatabaseException, TimeOutException;
}
