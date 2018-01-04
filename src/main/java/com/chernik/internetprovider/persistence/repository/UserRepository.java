package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException;

    Long create(User user) throws DatabaseException, TimeOutException;

    void updatePassword(User user, String newPassword) throws DatabaseException, TimeOutException;

    Page<User> getUsers(Pageable pageable) throws DatabaseException, TimeOutException;

    void banUser(User user, boolean baned) throws DatabaseException, TimeOutException;

    boolean isUserWithIdExists(Long id) throws DatabaseException, TimeOutException;
}
