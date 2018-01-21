package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

public interface TariffPlanDiscountService {
    void create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;
}
