package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import java.util.List;

public interface DiscountService {
    Long create(Discount discount) throws BaseException;

    void update(Discount discount) throws BaseException;

    void remove(Long id) throws BaseException;

    Page<Discount> getPage(Pageable pageable) throws BaseException;

    List<Discount> getAll() throws BaseException;

    Discount getById(Long id) throws BaseException;
}
