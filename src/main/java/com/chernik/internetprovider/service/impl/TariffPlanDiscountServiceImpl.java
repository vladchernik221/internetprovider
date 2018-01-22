package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanDiscountRepository;
import com.chernik.internetprovider.service.TariffPlanDiscountService;

import java.util.List;

@Service
public class TariffPlanDiscountServiceImpl implements TariffPlanDiscountService {

    @Autowired
    private TariffPlanDiscountRepository tariffPlanDiscountRepository;

    @Override
    public void create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        List<Discount> discounts = tariffPlan.getDiscounts();

        if(!discounts.isEmpty()) {
            tariffPlanDiscountRepository.create(tariffPlan.getTariffPlanId(), discounts);
        }
    }

    @Override
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        if (tariffPlanDiscountRepository.existByTariffPlanId(tariffPlan.getTariffPlanId())) {
            tariffPlanDiscountRepository.remove(tariffPlan.getTariffPlanId());
        }
        create(tariffPlan);
    }
}
