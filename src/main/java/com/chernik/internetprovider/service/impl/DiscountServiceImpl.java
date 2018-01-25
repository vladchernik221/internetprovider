package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.*;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.DiscountRepository;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.DiscountService;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ContractService contractService;

    public void setDiscountRepository(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }


    @Override
    public Long create(Discount discount) throws BaseException {
        if (!discountRepository.existWithName(discount.getName())) {
            return discountRepository.create(discount);
        } else {
            throw new UnableSaveEntityException(String.format("Discount with name: %s already exist", discount.getName()));
        }
    }

    @Override
    public void update(Discount discount) throws BaseException {
        if (discountRepository.existWithId(discount.getDiscountId())) {
            discountRepository.update(discount);
        } else {
            throw new EntityNotFoundException(String.format("Discount with id: %s does not exist", discount.getDiscountId()));
        }
    }

    @Override
    public Page<Discount> getPage(Pageable pageable) throws BaseException {
        return discountRepository.getPage(pageable);
    }

    @Override
    public List<Discount> getAll() throws BaseException {
        return discountRepository.getAll();
    }

    @Override
    public Discount getById(Long id) throws BaseException {
        Optional<Discount> discount = discountRepository.getById(id);
        if (!discount.isPresent()) {
            throw new EntityNotFoundException(String.format("Discount with id=%d does not exist", id));
        }
        return discount.get();
    }

    @Override
    public void remove(Long id) throws BaseException {
        if (!discountRepository.existWithId(id)) {
            throw new EntityNotFoundException(String.format("Discount with id: %s does not exist", id));
        }

        discountRepository.remove(id);
    }

    @Override
    public List<Discount> getAllByTariffPlan(TariffPlan tariffPlan) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Long tariffPlanId = tariffPlan.getTariffPlanId();

        if (!contractService.notExistById(tariffPlanId)) {
            throw new EntityNotFoundException(String.format("Tariff plan with id=%d not found", tariffPlanId));
        }

        return discountRepository.getByTariffPlanId(tariffPlanId);
    }
}
