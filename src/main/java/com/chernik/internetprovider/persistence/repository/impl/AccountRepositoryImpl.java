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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private static final String GET_BY_ID = "SELECT `balance`, `used_traffic`, `contract_annex_id` FROM `account` WHERE `contract_annex_id`=?";

    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Optional<Account> getById(Long contractAnnexId) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(contractAnnexId, this::createStatementForGettingById, this::createAccount);
    }

    private PreparedStatement createStatementForGettingById(Connection connection, Long contractAnnexId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
        statement.setLong(1, contractAnnexId);
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
}
