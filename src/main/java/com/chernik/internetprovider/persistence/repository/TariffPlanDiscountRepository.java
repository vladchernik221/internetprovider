package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Discount;

import java.util.List;

public interface TariffPlanDiscountRepository {
    void create(Long tariffPlanId, List<Discount> discounts) throws DatabaseException, TimeOutException;

    void remove(Long tariffPlanId) throws DatabaseException, TimeOutException;

    boolean existByTariffPlanId(Long tariffPlanId) throws DatabaseException, TimeOutException;
}
