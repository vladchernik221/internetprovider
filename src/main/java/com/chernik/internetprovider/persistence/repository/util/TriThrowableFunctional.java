package com.chernik.internetprovider.persistence.repository.util;

import java.sql.SQLException;

@FunctionalInterface
public interface TriThrowableFunctional<T, U, E, R> {
    R apply(T t, U u, E e) throws SQLException;
}
