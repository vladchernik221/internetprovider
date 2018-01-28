package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository {
    Long create(Discount discount) throws DatabaseException, TimeOutException;

    void update(Discount discount) throws DatabaseException, TimeOutException;

    Page<Discount> getPage(Pageable pageable) throws DatabaseException, TimeOutException;

    List<Discount> getAll() throws DatabaseException, TimeOutException;

    Optional<Discount> getById(Long id) throws DatabaseException, TimeOutException;

    void remove(Long id) throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;

    boolean existWithName(String name) throws DatabaseException, TimeOutException;

    boolean existWithNameAndNotId(Long id, String name) throws DatabaseException, TimeOutException;

    List<Discount> getByTariffPlanId(Long tariffPlanId) throws DatabaseException, TimeOutException;
}
