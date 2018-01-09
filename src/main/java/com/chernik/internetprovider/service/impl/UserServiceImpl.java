package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import com.chernik.internetprovider.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> authenticate(String login, String password) throws BaseException {
        LOGGER.log(Level.DEBUG, "Authentication user with login: {}", login);
        return userRepository.getUserByLoginAndPassword(login, password);
    }
}
