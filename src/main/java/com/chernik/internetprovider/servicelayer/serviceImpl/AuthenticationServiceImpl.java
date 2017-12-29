package com.chernik.internetprovider.servicelayer.serviceImpl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.FrontControllerException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import com.chernik.internetprovider.servicelayer.service.AuthenticationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final static Logger LOGGER = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User authenticate(String login, String password) throws FrontControllerException {
        LOGGER.log(Level.DEBUG, "Authentication user with login: {}", login);
        User user;
        try {
            user = userRepository.getUserByLoginAndPassword(login, password);
        } catch (DatabaseException e) {
            throw new FrontControllerException(e.getMessage(), "500", e);
        } catch (TimeOutException e) {
            throw new FrontControllerException("Timeout while waiting for database connection", "408", e);
        }
        return user;
    }
}
