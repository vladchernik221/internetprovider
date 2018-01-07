package com.chernik.internetprovider.persistence.repository.util;

import java.sql.SQLException;

@FunctionalInterface
public interface BiThrowableFunctional<T, U, R> {
    R apply(T t, U u) throws SQLException;
}
