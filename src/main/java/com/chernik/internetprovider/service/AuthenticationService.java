package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;

public interface AuthenticationService {
    User authenticate(String login, String password) throws BaseException;
}
