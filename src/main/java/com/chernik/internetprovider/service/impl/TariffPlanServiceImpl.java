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
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (!tariffPlanRepository.existWithName(tariffPlan.getName())) {
            return tariffPlanRepository.create(tariffPlan);
        } else {
            throw new UnableSaveEntityException(String.format("Tariff plan with name: %s already exist", tariffPlan.getName()));
        }
    }

    @Override
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, EntityNotFoundException {//TODO validating id name pair
        if (tariffPlanRepository.existWithId(tariffPlan.getTariffPlanId())) {
            tariffPlanRepository.update(tariffPlan);
        } else {
            throw new EntityNotFoundException(String.format("Tariff plan with name: %s does not exist", tariffPlan.getName()));
        }
    }

    @Override
    public Page<TariffPlan> getPage(Pageable pageable, Boolean archived) throws DatabaseException, TimeOutException {
        return tariffPlanRepository.getPage(archived, pageable);
    }

    @Override
    public TariffPlan getById(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Optional<TariffPlan> tariffPlan = tariffPlanRepository.getById(id);
        if (!tariffPlan.isPresent()) {
            throw new EntityNotFoundException(String.format("Tariff plan with id=%d does not exist", id));
        }
        return tariffPlan.get();
    }

    @Override
    public void archive(Long id) throws TimeOutException, EntityNotFoundException, DatabaseException {
        TariffPlan tariffPlan = getById(id);
        tariffPlan.setArchived(!tariffPlan.getArchived());
        tariffPlanRepository.archive(tariffPlan);
    }
}
