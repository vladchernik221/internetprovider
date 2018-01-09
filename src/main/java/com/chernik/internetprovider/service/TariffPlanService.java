package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

public interface TariffPlanService {
    Long createNewTariffPlan(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, UnableSaveEntityException;

    void updateTariffPlan(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, UnableSaveEntityException, EntityNotFoundException;

    Page<TariffPlan> getTariffPlans(Pageable pageable, Boolean archived) throws DatabaseException, TimeOutException;

    TariffPlan getTariffPlan(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException;

    void archiveTariffPlan(Long id) throws TimeOutException, EntityNotFoundException, DatabaseException;
}
