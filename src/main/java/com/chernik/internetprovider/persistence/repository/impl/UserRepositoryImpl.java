package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.entityfield.ContractField;
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
    private static final Logger LOGGER = LogManager.getLogger(UserRepositoryImpl.class);


    private static final String GET_USER_BY_LOGIN_AND_PASSWORD = "SELECT `user_id`, `login`, `role`, `blocked`, `contract_id` FROM `user` WHERE `login`=? AND `password`=MD5(?)";

    private static final String GET_USER_BY_ID = "SELECT `user_id`, `login`, `role`, `blocked` FROM `user` WHERE `user_id`=?";

    private static final String CREATE_USER = "INSERT INTO `user`(`login`, `password`, `role`, `contract_id`) VALUES(?,MD5(?),?,?)";

    private static final String UPDATE_PASSWORD = "UPDATE `user` SET `password`=MD5(?) WHERE `user_id`=?";

    private static final String GET_USER_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `user`";

    private static final String GET_USERS_PAGE = "SELECT `user_id`, `login`, `role`, `blocked`, `contract_id` FROM `user` LIMIT ? OFFSET ?";

    private static final String GET_USER_WITH_ROLE_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `user` WHERE `role`=?";

    private static final String GET_USERS_WITH_ROLE_PAGE = "SELECT `user_id`, `login`, `role`, `blocked`, `contract_id` FROM `user` WHERE `role`=? LIMIT ? OFFSET ?";

    private static final String BLOCK_USER = "UPDATE `user` u SET u.blocked=NOT u.blocked WHERE u.user_id=?";

    private static final String IS_EXIST_USER_WITH_LOGIN = "SELECT EXISTS(SELECT 1 FROM `user` WHERE `login`=?)";

    private static final String EXIST_BY_ID = "SELECT EXISTS(SELECT 1 FROM `user` WHERE `user_id`=?)";


    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Optional<User> getByLoginAndPassword(String login, String password) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(login, password, this::createStatementForGettingByLoginAndPassword, this::createUser);
    }

    private PreparedStatement createStatementForGettingByLoginAndPassword(Connection connection, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USER_BY_LOGIN_AND_PASSWORD);
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
        PreparedStatement statement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.getLogin());
        if (user.getPassword() != null) {
            statement.setString(2, user.getPassword());
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        statement.setString(3, user.getUserRole().toString());
        if (user.getContract() != null) {
            statement.setLong(4, user.getContract().getContractId());
        } else {
            statement.setNull(4, Types.INTEGER);
        }
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void updatePassword(Long userId, String newPassword) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(userId, newPassword, this::createStatementForUpdatingPassword);
    }

    private PreparedStatement createStatementForUpdatingPassword(Connection connection, Long userId, String newPassword) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD);
        statement.setString(1, newPassword);
        statement.setLong(2, userId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<User> getPage(Pageable pageable) throws DatabaseException, TimeOutException {
        return commonRepository.getPage(pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createUser);
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
    public Page<User> getPageWithRole(Pageable pageable, String userRole) throws DatabaseException, TimeOutException {
        return commonRepository.getPage(userRole, pageable, this::createCountWithRoleStatement, this::createPreparedStatementForGettingWithRole, this::createUser);
    }

    private PreparedStatement createCountWithRoleStatement(Connection connection, String userRole, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USER_WITH_ROLE_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        statement.setString(2, userRole);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private PreparedStatement createPreparedStatementForGettingWithRole(Connection connection, String userRole, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USERS_WITH_ROLE_PAGE);
        statement.setString(1, userRole);
        statement.setInt(2, pageable.getPageSize());
        statement.setInt(3, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    @Override
    public Optional<User> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createStatementForGettingById, this::createUser);
    }

    private PreparedStatement createStatementForGettingById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void block(Long id) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(id, this::createStatementForBlock);
    }

    private PreparedStatement createStatementForBlock(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(BLOCK_USER);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean existWithLogin(String login) throws DatabaseException, TimeOutException {
        return commonRepository.exist(login, this::createPreparedStatementForExistByLogin);
    }

    private PreparedStatement createPreparedStatementForExistByLogin(Connection connection, String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_USER_WITH_LOGIN);
        statement.setString(1, login);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong(UserField.USER_ID.toString()));
        user.setLogin(resultSet.getString(UserField.LOGIN.toString()));
        user.setUserRole(UserRole.valueOf(resultSet.getString(UserField.ROLE.toString())));
        user.setBlocked(resultSet.getBoolean(UserField.BLOCKED.toString()));
        Long contractId = (Long) resultSet.getObject(ContractField.CONTRACT_ID.toString());

        if (contractId != null) {
            Contract contract = new Contract();
            contract.setContractId(contractId);
            user.setContract(contract);
        }
        return user;
    }


    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createStatementForExistById);
    }

    private PreparedStatement createStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXIST_BY_ID);
        statement.setLong(1, id);
        return statement;
    }
}
