package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;

import java.util.Optional;

public interface DiscountRepository {
    Long create(Discount discount) throws DatabaseException, TimeOutException;

    void update(Discount discount) throws DatabaseException, TimeOutException;

    Page<Discount> getDiscounts(Pageable pageable) throws DatabaseException, TimeOutException;

    Optional<Discount> getDiscount(Long id) throws DatabaseException, TimeOutException;

    void remove(Discount discount) throws DatabaseException, TimeOutException;

    boolean isDiscountExistWithId(Long id) throws DatabaseException, TimeOutException;
}
