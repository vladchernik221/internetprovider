package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.User;

public interface UserRepository {
    User getUserByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException;
}
