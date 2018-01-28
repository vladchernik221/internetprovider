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

    void archive(Long service) throws DatabaseException, TimeOutException;

    Page<Service> getPage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException;

    Optional<Service> getById(Long id) throws DatabaseException, TimeOutException;

    boolean existWithName(String name) throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;

    boolean existWithNameAndNotId(Long id, String name) throws DatabaseException, TimeOutException;

    Page<Service> getPageByContractAnnexId(Long id, Pageable pageable) throws DatabaseException, TimeOutException;
}
