package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import java.util.Optional;

public interface TariffPlanRepository {
    Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    void archived(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    Page<TariffPlan> getTariffPlanPage(Boolean archived, Pageable pageable) throws DatabaseException, TimeOutException;

    Optional<TariffPlan> getById(Long id) throws DatabaseException, TimeOutException;

    boolean isTariffPlanWithNameExist(String name) throws DatabaseException, TimeOutException;

    boolean isTariffPlanWithIdExist(Long id) throws DatabaseException, TimeOutException;
}
