package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;

public interface DiscountRepository {
    Long create(Discount discount);

    void update(Discount discount);

    Page<Discount> getDiscounts(Pageable pageable);

    Discount getDiscount(Long id);

    void remove(Discount discount);

    boolean isDiscountExistWithId(Long id);
}
