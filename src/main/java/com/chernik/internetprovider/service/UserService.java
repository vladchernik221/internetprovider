package com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> authenticate(String login, String password) throws BaseException;

    Long create(User user) throws DatabaseException, TimeOutException, UnableSaveEntityException;

    Page<User> getPage(Pageable pageable, String userRole) throws DatabaseException, TimeOutException;

    void block(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException;

    void changePassword(Long userId, String newPassword) throws DatabaseException, TimeOutException, EntityNotFoundException;
}
