package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.context.Transactional;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.service.TariffPlanDiscountService;
import com.chernik.internetprovider.service.TariffPlanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

//TODO archived
@Service
public class TariffPlanServiceImpl implements TariffPlanService {
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanServiceImpl.class);

    @Autowired
    private TariffPlanRepository tariffPlanRepository;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private TariffPlanDiscountService tariffPlanDiscountService;

    @Override
    @Transactional
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (!tariffPlanRepository.existWithName(tariffPlan.getName())) {
            Long generatedID = tariffPlanRepository.create(tariffPlan);
            tariffPlan.setTariffPlanId(generatedID);
            tariffPlanDiscountService.create(tariffPlan);
            return generatedID;
        } else {
            throw new UnableSaveEntityException(String.format("Tariff plan with name: %s already exist", tariffPlan.getName()));
        }
    }

    @Override
    @Transactional
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, EntityNotFoundException {//TODO validating id name pair
        if (tariffPlanRepository.existWithId(tariffPlan.getTariffPlanId())) {
            tariffPlanRepository.update(tariffPlan);
            tariffPlanDiscountService.update(tariffPlan);
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
        Optional<TariffPlan> tariffPlanOptional = tariffPlanRepository.getById(id);
        if (!tariffPlanOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Tariff plan with id=%d does not exist", id));
        }
        TariffPlan tariffPlan = tariffPlanOptional.get();
        List<Discount> discounts = discountService.getAllByTariffPlan(tariffPlan);
        tariffPlan.setDiscounts(discounts);
        return tariffPlan;
    }

    @Override
    public void archive(Long id) throws TimeOutException, EntityNotFoundException, DatabaseException {
        TariffPlan tariffPlan = getById(id);
        tariffPlan.setArchived(!tariffPlan.getArchived());
        tariffPlanRepository.archive(tariffPlan);
    }

    @Override
    public List<TariffPlan> getAllNotArchived() throws DatabaseException, TimeOutException {
        return tariffPlanRepository.getAllNotArchived();
    }

    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        return tariffPlanRepository.existWithId(id);
    }
}
