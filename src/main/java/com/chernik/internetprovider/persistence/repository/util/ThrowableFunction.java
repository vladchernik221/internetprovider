package com.chernik.internetprovider.persistence.repository.util;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowableFunction<T, R> {
    R apply(T t) throws SQLException;
}
