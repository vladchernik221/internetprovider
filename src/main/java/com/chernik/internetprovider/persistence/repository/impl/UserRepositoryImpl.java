package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.entityfield.UserField;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    private ConnectionPool connectionPool;


    @Override
    public Optional<User> getUserByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Finding user with login: {}", login);
        User user = null;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createStatementForGettingByLoginAndPassword(connection, login, password);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                user = createUser(resultSet);
            }
            LOGGER.log(Level.TRACE, "Found user: {}", user);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return Optional.ofNullable(user);
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
        LOGGER.log(Level.TRACE, "Inserting user {}", user);
        Connection connection = connectionPool.getConnection();
        Long generatedId;
        try (PreparedStatement statement = createStatementForInserting(connection, user)) {
            int affectedRow = statement.executeUpdate();
            if (affectedRow == 0) {
                throw new DatabaseException("Creating user failed, no rows affected.");
            }
            generatedId = getGeneratedId(statement);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Inserting user complete successful");
        return generatedId;
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
        LOGGER.log(Level.TRACE, "Updating user {}", user);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createStatementForUpdatingPassword(connection, user, newPassword)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating tariff plan failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Updating user complete successful");
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
        LOGGER.log(Level.TRACE, "Getting page of user. Page number is {}, page size is {}", pageable.getPageNumber(), pageable.getPageSize());
        Connection connection = connectionPool.getConnection();
        Page<User> userPage = new Page<>();
        try (PreparedStatement countStatement = createCountStatement(connection, pageable);
             ResultSet countResultSet = countStatement.executeQuery();
             PreparedStatement statement = createPreparedStatementForGetting(connection, pageable);
             ResultSet resultSet = statement.executeQuery()) {

            if (countResultSet.next()) {
                userPage.setPagesCount(countResultSet.getInt(1));
            } else {
                throw new DatabaseException("Getting user pages count failed");
            }

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(createUser(resultSet));//TODO contract
            }

            userPage.setData(users);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting users complete successful");
        return userPage;
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
        LOGGER.log(Level.TRACE, "Ban user. Set baned to {}", baned);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createStatementForBan(connection, user, baned)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Ban user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private PreparedStatement createStatementForBan(Connection connection, User user, boolean baned) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(BAN_USER);
        statement.setBoolean(1, baned);
        statement.setLong(2, user.getUserId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isUserWithIdExists(Long id) throws DatabaseException, TimeOutException {//TODO duplicate
        LOGGER.log(Level.TRACE, "Check tariff plan existing by id {}", id);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForExistById(connection, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    private User createUser(ResultSet resultSet) throws SQLException {
        User user;
        user = new User();
        user.setUserId(resultSet.getLong(UserField.USER_ID.toString()));
        user.setLogin(resultSet.getString(UserField.LOGIN.toString()));
        user.setUserRole(UserRole.valueOf(resultSet.getString(UserField.ROLE.toString())));
        user.setBlocked(resultSet.getBoolean(UserField.BLOCKED.toString()));
        return user;
    }

    private Long getGeneratedId(PreparedStatement statement) throws DatabaseException {//TODO something with duplicate
        Long generatedId;
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getLong(1);
            } else {
                throw new DatabaseException("Creating failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        }
        LOGGER.log(Level.TRACE, "Generated id: {}", generatedId);
        return generatedId;
    }
}
