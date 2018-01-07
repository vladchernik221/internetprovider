package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.entityfield.UserField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final static Logger LOGGER = LogManager.getLogger(UserRepositoryImpl.class);


    private final static String SELECT_USER_BY_LOGIN_AND_PASSWORD =
            "SELECT `user_id`, `login`, `role`, `blocked` FROM `user` WHERE `login`=? AND `password`=MD5(?)";

    private final static String CREATE_USER = "INSERT INTO `user`(`login`, `password`, `role`, `blocked`, `contract_id`) VALUES(?,MD5(?),?,?,?)";

    private final static String UPDATE_PASSWORD = "UPDATE `user` SET `password`=MD5(?) WHERE `user_id`=?";

    private final static String GET_USER_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `user`";

    private final static String GET_USERS_PAGE = "SELECT `login`, `role`, `blocked`, `contract_id` FROM `user` LIMIT ? OFFSET ?";

    private final static String BAN_USER = "UPDATE `user` SET `blocked`=? WHERE `user_id`=?";

    private final static String IS_EXIST_TARIFF_PLAN_WITH_ID = "SELECT EXISTS(SELECT 1 FROM `user` WHERE `user_id`=?)";


    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Optional<User> getUserByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(login, password, this::createStatementForGettingByLoginAndPassword, this::createUser);
    }

    private PreparedStatement createStatementForGettingByLoginAndPassword(Connection connection, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_LOGIN_AND_PASSWORD);
        statement.setString(1, login);
        statement.setString(2, password);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Long create(User user) throws DatabaseException, TimeOutException {
        return commonRepository.create(user, this::createStatementForInserting);
    }

    private PreparedStatement createStatementForInserting(Connection connection, User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_USER);
        statement.setString(1, user.getLogin());
        if (user.getPassword() != null) {
            statement.setString(2, user.getPassword());
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        statement.setString(3, user.getUserRole().toString());
        statement.setBoolean(4, user.getBlocked());
        if (user.getContract() != null) {
            statement.setLong(5, user.getContract().getContractId());
        } else {
            statement.setNull(5, Types.INTEGER);
        }
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void updatePassword(User user, String newPassword) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(user, newPassword, this::createStatementForUpdatingPassword);
    }

    private PreparedStatement createStatementForUpdatingPassword(Connection connection, User user, String newPassword) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD);
        statement.setString(1, newPassword);
        statement.setLong(2, user.getUserId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<User> getUsers(Pageable pageable) throws DatabaseException, TimeOutException {
        return commonRepository.getEntityPage(pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createUser);
    }

    private PreparedStatement createCountStatement(Connection connection, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USER_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USERS_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void banUser(User user, boolean baned) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(user, baned, this::createStatementForBan);
    }

    private PreparedStatement createStatementForBan(Connection connection, User user, boolean baned) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(BAN_USER);
        statement.setBoolean(1, baned);
        statement.setLong(2, user.getUserId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isUserWithIdExists(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createPreparedStatementForExistById);
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong(UserField.USER_ID.toString()));
        user.setLogin(resultSet.getString(UserField.LOGIN.toString()));
        user.setUserRole(UserRole.valueOf(resultSet.getString(UserField.ROLE.toString())));
        user.setBlocked(resultSet.getBoolean(UserField.BLOCKED.toString()));
        return user;
    }
}
