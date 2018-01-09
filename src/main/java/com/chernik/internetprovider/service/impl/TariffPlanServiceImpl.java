package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Service
public class TariffPlanServiceImpl implements com.chernik.internetprovider.service.TariffPlanService {
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanServiceImpl.class);

    @Autowired
    private TariffPlanRepository tariffPlanRepository;

    @Override
    public Long createNewTariffPlan(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (!tariffPlanRepository.isTariffPlanWithNameExist(tariffPlan.getName())) {
            return tariffPlanRepository.create(tariffPlan);
        } else {
            throw new UnableSaveEntityException(String.format("Tariff plan with name: %s already exist", tariffPlan.getName()));
        }
    }

    @Override
    public void updateTariffPlan(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, EntityNotFoundException {//TODO validating id name pair
        if (tariffPlanRepository.isTariffPlanWithIdExist(tariffPlan.getTariffPlanId())) {
            tariffPlanRepository.update(tariffPlan);
        } else {
            throw new EntityNotFoundException(String.format("Tariff plan with name: %s does not exist", tariffPlan.getName()));
        }
    }

    @Override
    public Page<TariffPlan> getTariffPlans(Pageable pageable, Boolean archived) throws DatabaseException, TimeOutException {
        return tariffPlanRepository.getTariffPlanPage(archived, pageable);
    }

    @Override
    public TariffPlan getTariffPlan(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Optional<TariffPlan> tariffPlan = tariffPlanRepository.getById(id);
        if (!tariffPlan.isPresent()) {
            throw new EntityNotFoundException(String.format("Tariff plan with id=%d does not exist", id));
        }
        return tariffPlan.get();
    }

    @Override
    public void archiveTariffPlan(Long id) throws TimeOutException, EntityNotFoundException, DatabaseException {
        TariffPlan tariffPlan = getTariffPlan(id);
        tariffPlan.setArchived(!tariffPlan.getArchived());
        tariffPlanRepository.archive(tariffPlan);
    }
}
