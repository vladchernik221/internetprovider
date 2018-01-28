package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;

public interface Mapper<T> {
    T create(HttpServletRequest request) throws BadRequestException;
}
