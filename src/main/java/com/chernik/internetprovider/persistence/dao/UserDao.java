package com.chernik.internetprovider.persistence.dao;

import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.repository.UserRepository;

public class UserDao implements UserRepository {
    private ConnectionPool connectionPool;

    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
