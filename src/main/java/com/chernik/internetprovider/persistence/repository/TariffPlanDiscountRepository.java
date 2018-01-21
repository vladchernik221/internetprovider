package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;

public interface TariffPlanDiscountRepository {
    void create(Long tariffPlanId, Long discountId) throws DatabaseException, TimeOutException;

    void remove(Long tariffPlanId) throws DatabaseException, TimeOutException;

    boolean existByTariffPlanId(Long tariffPlanId) throws DatabaseException, TimeOutException;
}
