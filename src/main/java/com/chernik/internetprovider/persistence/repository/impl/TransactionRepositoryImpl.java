package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.entity.TransactionType;
import com.chernik.internetprovider.persistence.entityfield.TransactionField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private static final Logger LOGGER = LogManager.getLogger(TransactionRepositoryImpl.class);

    private static final String CREATE_TRANSACTION = "INSERT INTO `transaction`(`type`, `amount`, `contract_annex_id`) VALUES (?,?,?)";

    private static final String GET_TRANSACTION_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `transaction` WHERE `contract_annex_id` = ?";

    private static final String GET_TRANSACTION_PAGE = "SELECT `transaction_id`, `type`, `amount`, `date` FROM `transaction` WHERE `contract_annex_id` = ? LIMIT ? OFFSET ?";


    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Long create(Transaction transaction) throws DatabaseException, TimeOutException {
        return commonRepository.create(transaction, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, Transaction transaction) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, transaction.getType().toString());
        statement.setBigDecimal(2, transaction.getAmount());
        statement.setLong(3, transaction.getAccount().getContractAnnex().getContractAnnexId());
        return statement;
    }


    @Override
    public Page<Transaction> getPage(Long contractAnnexId, Pageable pageable) throws DatabaseException, TimeOutException {
        return commonRepository.getPage(contractAnnexId, pageable, this::createPreparedStatementForGettingPageCount, this::createPreparedStatementForGettingPage, this::createTransaction);
    }

    private PreparedStatement createPreparedStatementForGettingPageCount(Connection connection, Long contractAnnexId, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TRANSACTION_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        statement.setLong(2, contractAnnexId);
        return statement;
    }

    private PreparedStatement createPreparedStatementForGettingPage(Connection connection, Long contractAnnexId, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TRANSACTION_PAGE);
        statement.setLong(1, contractAnnexId);
        statement.setInt(2, pageable.getPageSize());
        statement.setInt(3, pageable.getPageSize() * pageable.getPageNumber());
        return statement;
    }

    private Transaction createTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getLong(TransactionField.TRANSACTION_ID.toString()));
        transaction.setAmount(resultSet.getBigDecimal(TransactionField.AMOUNT.toString()));
        transaction.setType(TransactionType.valueOf(resultSet.getString(TransactionField.TYPE.toString())));
        transaction.setDate(resultSet.getDate(TransactionField.DATE.toString()));
        return transaction;
    }
}
