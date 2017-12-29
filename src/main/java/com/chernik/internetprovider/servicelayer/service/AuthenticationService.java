package com.chernik.internetprovider.servicelayer.service;

import com.chernik.internetprovider.exception.FrontControllerException;
import com.chernik.internetprovider.persistence.entity.User;

public interface AuthenticationService {
    User authenticate(String login, String password) throws FrontControllerException;
}
