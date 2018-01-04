package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> authenticate(String login, String password) throws BaseException;
}
