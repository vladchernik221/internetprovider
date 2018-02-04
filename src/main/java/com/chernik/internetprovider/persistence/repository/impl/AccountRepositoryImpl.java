package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entityfield.AccountField;
import com.chernik.internetprovider.persistence.repository.AccountRepository;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private static final Logger LOGGER = LogManager.getLogger(AccountRepositoryImpl.class);


    private static final String GET_BY_ID = "SELECT `balance`, `used_traffic`, `contract_annex_id` FROM `account` WHERE `contract_annex_id`=?";

    private static final String ADD_USED_TRAFFIC = "UPDATE `account` a SET a.used_traffic=a.used_traffic+? WHERE a.contract_annex_id=?";

    private static final String EXISTS_BY_ID = "SELECT EXISTS(SELECT 1 FROM `account` WHERE `contract_annex_id`=?)";

    private static final String IS_USER_OWNER = "SELECT EXISTS(SELECT 1 FROM `contract_annex` ca JOIN `contract` c ON ca.contract_id = c.contract_id JOIN `user` u ON c.contract_id = u.contract_id WHERE ca.contract_annex_id=? AND u.user_id=?)";


    @Autowired
    private CommonRepository commonRepository;

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public Optional<Account> getById(Long contractAnnexId) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting account for contract annex with ID {}", contractAnnexId);
        return commonRepository.getByParameters(contractAnnexId, this::createStatementForGettingById, this::createAccount);
    }

    private PreparedStatement createStatementForGettingById(Connection connection, Long contractAnnexId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
        statement.setLong(1, contractAnnexId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private Account createAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setBalance(resultSet.getBigDecimal(AccountField.BALANCE.toString()));
        account.setUsedTraffic(resultSet.getInt(AccountField.USED_TRAFFIC.toString()));
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(resultSet.getLong(AccountField.CONTRACT_ANNEX_ID.toString()));
        account.setContractAnnex(contractAnnex);
        return account;
    }


    @Override
    public void addUsedTraffic(Long accountId, Integer usedTraffic) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Adding used traffic {} to account with ID {}", usedTraffic, accountId);
        commonRepository.executeUpdate(accountId, usedTraffic, this::createStatementForAddUsedTraffic);
    }

    private PreparedStatement createStatementForAddUsedTraffic(Connection connection, Long contractAnnexId, Integer usedTraffic) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ADD_USED_TRAFFIC);
        statement.setInt(1, usedTraffic);
        statement.setLong(2, contractAnnexId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing of account with ID {}", id);
        return commonRepository.exist(id, this::createStatementForExistsById);
    }

    private PreparedStatement createStatementForExistsById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    @Override
    public boolean isUserOwner(Long contractAnnexId, Long userId) throws DatabaseException, TimeOutException {
        return commonRepository.exist(contractAnnexId, userId, this::createStatementForIsUserOwner);
    }

    private PreparedStatement createStatementForIsUserOwner(Connection connection, Long contractAnnexId, Long userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_USER_OWNER);
        statement.setLong(1, contractAnnexId);
        statement.setLong(2, userId);
        return statement;
    }
}
