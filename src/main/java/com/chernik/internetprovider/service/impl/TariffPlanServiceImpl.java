package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.FrontControllerException;
import com.chernik.internetprovider.exception.ServiceException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import com.chernik.internetprovider.service.TariffPlanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Service
public class TariffPlanServiceImpl implements TariffPlanService {
    private final static Logger LOGGER = LogManager.getLogger(TariffPlanServiceImpl.class);

    @Autowired
    private TariffPlanRepository tariffPlanRepository;

    @Override
    public Long createNewTariffPlan(TariffPlan tariffPlan) throws FrontControllerException, ServiceException {
        try {
            if (!tariffPlanRepository.isTariffPlanWithNameExist(tariffPlan.getName())) {
                return tariffPlanRepository.create(tariffPlan);
            } else {
                throw new ServiceException(String.format("Tariff plan with name: %s already exist", tariffPlan.getName()));
            }
        } catch (DatabaseException e) {
            throw new FrontControllerException("500", "Error while working with database", e);
        } catch (TimeOutException e) {
            throw new FrontControllerException("408", "Time to connect to database came out", e);
        }
    }

    @Override
    public void updateTariffPlan(TariffPlan tariffPlan) throws FrontControllerException, ServiceException {
        try {
            if (tariffPlanRepository.isTariffPlanWithNameExist(tariffPlan.getName())) {
                tariffPlanRepository.update(tariffPlan);
            } else {
                throw new ServiceException(String.format("Tariff plan with name: %s does not exist", tariffPlan.getName()));
            }
        } catch (DatabaseException e) {
            throw new FrontControllerException("500", "Error while working with database", e);
        } catch (TimeOutException e) {
            throw new FrontControllerException("408", "Time to connect to database came out", e);
        }
    }

    @Override
    public Page<TariffPlan> getTariffPlans(Pageable pageable, Boolean archived) throws FrontControllerException {
        try {
            return tariffPlanRepository.getTariffPlanPage(archived, pageable);
        } catch (DatabaseException e) {
            throw new FrontControllerException("500", "Error while working with database", e);
        } catch (TimeOutException e) {
            throw new FrontControllerException("408", "Time to connect to database came out", e);
        }
    }

    @Override
    public TariffPlan getTariffPlan(Long id) throws FrontControllerException, ServiceException {
        Optional<TariffPlan> tariffPlan;
        try {
            tariffPlan = tariffPlanRepository.getById(id);
            if (!tariffPlan.isPresent()) {
                throw new ServiceException(String.format("Tariff plan with id=%d does not exist", id));
            }
        } catch (DatabaseException e) {
            throw new FrontControllerException("500", "Error while working with database", e);
        } catch (TimeOutException e) {
            throw new FrontControllerException("408", "Time to connect to database came out", e);
        }
        return tariffPlan.get();
    }

    @Override
    public void archiveTariffPlan(TariffPlan tariffPlan) throws FrontControllerException, ServiceException {
        tariffPlan.setArchived(!tariffPlan.getArchived());
        updateTariffPlan(tariffPlan);
    }
}
