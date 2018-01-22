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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private static final Logger LOGGER = LogManager.getLogger(TransactionRepositoryImpl.class);

    private static final String CREATE_TRANSACTION = "INSERT INTO `transaction`(`type`, `amount`, `account_id`) VALUES (?,?,?)";

    private static final String GET_TRANSACTION_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `transaction`";

    private static final String GET_TRANSACTION_PAGE = "SELECT `transaction_id`, `type`, `amount`, `date` FROM `transaction` LIMIT ? OFFSET ?";


    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Long create(Transaction transaction) throws DatabaseException, TimeOutException {
        return commonRepository.create(transaction, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, Transaction transaction) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_TRANSACTION);
        statement.setString(1, transaction.getType().toString());
        statement.setBigDecimal(2, transaction.getAmount());
        statement.setLong(3, transaction.getAccount().getAccountId());
        return statement;
    }


    @Override
    public Page<Transaction> getPage(Pageable pageable) throws DatabaseException, TimeOutException {
        return commonRepository.getPage(pageable, this::createPreparedStatementForGettingPageCount, this::createPreparedStatementForGettingPage, this::createTransaction);
    }

    private PreparedStatement createPreparedStatementForGettingPageCount(Connection connection, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TRANSACTION_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        return statement;
    }

    private PreparedStatement createPreparedStatementForGettingPage(Connection connection, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TRANSACTION_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
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
