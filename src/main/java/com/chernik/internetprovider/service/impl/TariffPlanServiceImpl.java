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

@Service
public class TariffPlanServiceImpl implements TariffPlanService {
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanServiceImpl.class);

    @Autowired
    private TariffPlanRepository tariffPlanRepository;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private TariffPlanDiscountService tariffPlanDiscountService;

    public void setTariffPlanRepository(TariffPlanRepository tariffPlanRepository) {
        this.tariffPlanRepository = tariffPlanRepository;
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setTariffPlanDiscountService(TariffPlanDiscountService tariffPlanDiscountService) {
        this.tariffPlanDiscountService = tariffPlanDiscountService;
    }


    @Override
    @Transactional
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (tariffPlanRepository.existWithName(tariffPlan.getName())) {
            throw new UnableSaveEntityException(String.format("Tariff plan with name: %s already exist", tariffPlan.getName()));
        }

        Long generatedID = tariffPlanRepository.create(tariffPlan);
        tariffPlan.setTariffPlanId(generatedID);
        tariffPlanDiscountService.create(tariffPlan);
        return generatedID;
    }

    @Override
    @Transactional
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, EntityNotFoundException, UnableSaveEntityException {
        if (!tariffPlanRepository.existWithId(tariffPlan.getTariffPlanId())) {
            throw new EntityNotFoundException(String.format("Tariff plan with name: %s does not exist", tariffPlan.getName()));
        }
        if (!tariffPlanRepository.existWithIdAndName(tariffPlan.getTariffPlanId(), tariffPlan.getName()) && tariffPlanRepository.existWithName(tariffPlan.getName())) {
            throw new UnableSaveEntityException(String.format("Tariff plan with name=%s already exists", tariffPlan.getName()));
        }

        tariffPlanRepository.update(tariffPlan);
        tariffPlanDiscountService.update(tariffPlan);
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
    public void archive(Long id) throws TimeOutException, DatabaseException, EntityNotFoundException {
        if (!existWithId(id)) {
            throw new EntityNotFoundException(String.format("Tariff plan with id=%d does not exist", id));
        }

        tariffPlanRepository.archive(id);
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
