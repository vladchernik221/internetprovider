package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.entityfield.UserField;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final static Logger LOGGER = LogManager.getLogger(UserRepositoryImpl.class);

    private final static String SELECT_USER_BY_LOGIN_AND_PASSWORD =
            "SELECT `user_id`, `login`, `role`, `blocked` FROM `user` WHERE `login`=? AND `password`=MD5(?)";

    @Autowired
    private ConnectionPool connectionPool;

    @Override
    public User getUserByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Finding user with login: {}", login);
        User user = null;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatement(connection, login, password);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getLong(UserField.USER_ID.toString()));
                user.setLogin(resultSet.getString(UserField.LOGIN.toString()));
                user.setUserRole(UserRole.valueOf(resultSet.getString(UserField.ROLE.toString())));
                user.setBlocked(resultSet.getBoolean(UserField.BLOCKED.toString()));
            }
            LOGGER.log(Level.TRACE, "Found user: {}", user);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return user;
    }

    private PreparedStatement createPreparedStatement(Connection connection, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_LOGIN_AND_PASSWORD);
        statement.setString(1, login);
        statement.setString(2, password);
        LOGGER.log(Level.DEBUG, "Create statement with query: {}", statement);
        return statement;
    }
}
