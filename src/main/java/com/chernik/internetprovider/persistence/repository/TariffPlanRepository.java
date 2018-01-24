package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import java.util.List;
import java.util.Optional;

public interface TariffPlanRepository {
    Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException;

    void archive(Long id) throws DatabaseException, TimeOutException;

    Page<TariffPlan> getPage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException;

    Optional<TariffPlan> getById(Long id) throws DatabaseException, TimeOutException;

    boolean existWithName(String name) throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;

    List<TariffPlan> getAllNotArchived() throws DatabaseException, TimeOutException;

    boolean existWithIdAndName(Long id, String name) throws DatabaseException, TimeOutException;
}
