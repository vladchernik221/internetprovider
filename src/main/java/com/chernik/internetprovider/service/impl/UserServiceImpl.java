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
import com.chernik.internetprovider.persistence.entity.UserRole;
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

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> authenticate(String login, String password) throws BaseException {
        LOGGER.log(Level.DEBUG, "Authentication user with login: {}", login);
        return userRepository.getByLoginAndPassword(login, password);
    }

    @Override
    public Long create(User user) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (userRepository.existWithLogin(user.getLogin())) {
            throw new UnableSaveEntityException(String.format("User with login %s already", user.getLogin()));
        }

        return userRepository.create(user);
    }

    @Override
    public Page<User> getPage(Pageable pageable, UserRole userRole) throws DatabaseException, TimeOutException {
        if (userRole == null) {
            return userRepository.getPage(pageable);
        } else {
            return userRepository.getPageWithRole(pageable, userRole);
        }
    }

    @Override
    public void block(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        if (!userRepository.existWithId(id)) {
            throw new EntityNotFoundException(String.format("User with id %d was not found", id));
        }

        userRepository.block(id);
    }

    @Override
    public void changePassword(Long userId, String newPassword) throws DatabaseException, TimeOutException, EntityNotFoundException {
        if (!userRepository.existWithId(userId)) {
            throw new EntityNotFoundException(String.format("User with id %d was not found", userId));
        }

        userRepository.updatePassword(userId, newPassword);
    }
}
