package com.chernik.internetprovider.servicelayer.serviceImpl;

import com.chernik.internetprovider.persistence.repository.UserRepository;
import com.chernik.internetprovider.servicelayer.service.AuthenticationService;

public class AuthenticationServiceImpl implements AuthenticationService{
    private UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
