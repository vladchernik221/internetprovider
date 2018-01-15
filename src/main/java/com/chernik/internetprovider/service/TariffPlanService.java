package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;

import java.util.List;

public interface TariffPlanService {
    Long create(TariffPlan tariffPlan) throws BaseException;

    void update(TariffPlan tariffPlan) throws BaseException;

    Page<TariffPlan> getPage(Pageable pageable, Boolean archived) throws BaseException;

    TariffPlan getById(Long id) throws BaseException;

    void archive(Long id) throws BaseException;

    List<TariffPlan> getAllNotArchived() throws DatabaseException, TimeOutException;

    boolean existWithId(Long id) throws DatabaseException, TimeOutException;
}
