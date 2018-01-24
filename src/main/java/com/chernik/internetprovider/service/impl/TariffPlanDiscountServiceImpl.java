package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.context.Transactional;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanHasDiscountRepository;
import com.chernik.internetprovider.service.TariffPlanDiscountService;

import java.util.List;

@Service
public class TariffPlanDiscountServiceImpl implements TariffPlanDiscountService {

    @Autowired
    private TariffPlanHasDiscountRepository tariffPlanHasDiscountRepository;

    @Override
    public void create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        List<Discount> discounts = tariffPlan.getDiscounts();

        if (!discounts.isEmpty()) {
            tariffPlanHasDiscountRepository.create(tariffPlan.getTariffPlanId(), discounts);
        }
    }

    @Override
    @Transactional
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        if (tariffPlanHasDiscountRepository.existByTariffPlanId(tariffPlan.getTariffPlanId())) {
            tariffPlanHasDiscountRepository.remove(tariffPlan.getTariffPlanId());
        }
        create(tariffPlan);
    }
}
