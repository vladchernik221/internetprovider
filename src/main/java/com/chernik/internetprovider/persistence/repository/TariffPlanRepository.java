package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import java.util.List;

public interface TariffPlanRepository {
    Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    TariffPlan update(TariffPlan tariffPlan);

    List<TariffPlan> getPage(Pageable pageable);

    TariffPlan getById(Long id);

    void archivedTariffPlan(TariffPlan tariffPlan);
}
