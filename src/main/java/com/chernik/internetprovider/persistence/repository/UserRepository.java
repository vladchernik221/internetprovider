package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException;

    Long create(User user) throws DatabaseException, TimeOutException;

    void updatePassword(User user, String newPassword) throws DatabaseException, TimeOutException;

    Page<User> getPage(Pageable pageable) throws DatabaseException, TimeOutException;

    void ban(User user, boolean baned) throws DatabaseException, TimeOutException;

    boolean existsWithName(Long id) throws DatabaseException, TimeOutException;
}
