package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
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
        return userRepository.getByLoginAndPassword(login, password);
    }

    @Override
    public Long create(User user) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if(userRepository.existWithLogin(user.getLogin())) {
            throw new UnableSaveEntityException(String.format("User with login %s already", user.getLogin()));
        }
        return userRepository.create(user);
    }

    @Override
    public Page<User> getPage(Pageable pageable, String userRole) throws DatabaseException, TimeOutException {
        if(userRole.isEmpty()) {
            return userRepository.getPage(pageable);
        } else {
            return userRepository.getPageWithRole(pageable, userRole);
        }
    }

    @Override
    public void block(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Optional<User> user = userRepository.getById(id);
        if(!user.isPresent()) {
            throw new EntityNotFoundException(String.format("User with id %d was not found", id));
        }
        user.get().setBlocked(!user.get().getBlocked());
        userRepository.block(user.get());
    }
}
