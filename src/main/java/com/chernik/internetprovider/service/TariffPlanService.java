package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.FrontControllerException;
import com.chernik.internetprovider.exception.ServiceException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

public interface TariffPlanService {
    Long createNewTariffPlan(TariffPlan tariffPlan) throws FrontControllerException, ServiceException;

    void updateTariffPlan(TariffPlan tariffPlan) throws FrontControllerException, ServiceException;

    Page<TariffPlan> getTariffPlans(Pageable pageable, Boolean archived) throws FrontControllerException;

    TariffPlan getTariffPlan(Long id) throws FrontControllerException, ServiceException;

    void archiveTariffPlan(TariffPlan tariffPlan) throws FrontControllerException, ServiceException;
}
