package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.entityfield.LegalEntityClientInformationField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.LegalEntityClientInformationRepository;

import java.sql.*;
import java.util.Optional;

@Repository
public class LegalEntityClientInformationRepositoryImpl implements LegalEntityClientInformationRepository {

    private static final String CREATE_LEGAL_ENTITY_CLIENT_INFORMATION = "INSERT INTO `legal_entity_client_information`(`payer_account_number`, `checking_account`, `name`, `address`, `phone_number`) VALUES(?,?,?,?,?)";

    private static final String GET_BY_PAYER_ACCOUNT_NUMBER = "SELECT `legal_entity_client_information_id`, `payer_account_number`, `checking_account`, `name`, `address`, `phone_number` FROM `legal_entity_client_information` WHERE `payer_account_number`=?";

    private static final String UPDATE_LEGAL_ENTITY_CLIENT_INFORMATION = "UPDATE `legal_entity_client_information` SET `name`=?, `address`=?, `phone_number`=?, `checking_account`=? WHERE `payer_account_number`=?";

    private static final String GET_BY_ID = "SELECT `legal_entity_client_information_id`, `payer_account_number`, `checking_account`, `name`, `address`, `phone_number` FROM `legal_entity_client_information` WHERE `legal_entity_client_information_id`=?";


    @Autowired
    private CommonRepository commonRepository;

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public Long create(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException {
        return commonRepository.create(legalEntityClientInformation, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, LegalEntityClientInformation legalEntityClientInformation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_LEGAL_ENTITY_CLIENT_INFORMATION, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, legalEntityClientInformation.getPayerAccountNumber());
        statement.setString(2, legalEntityClientInformation.getCheckingAccount());
        statement.setString(3, legalEntityClientInformation.getName());
        statement.setString(4, legalEntityClientInformation.getAddress());
        statement.setString(5, legalEntityClientInformation.getPhoneNumber());
        return statement;
    }


    @Override
    public Optional<LegalEntityClientInformation> getByPayerAccountNumber(String payerAccountNumber) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(payerAccountNumber, this::createPreparedStatementForGettingByPayerAccountNumber, this::createLegalEntityClientInformation);
    }

    private PreparedStatement createPreparedStatementForGettingByPayerAccountNumber(Connection connection, String payerAccountNumber) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_PAYER_ACCOUNT_NUMBER);
        statement.setString(1, payerAccountNumber);
        return statement;
    }


    @Override
    public void update(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(legalEntityClientInformation, this::createPreparedStatementForUpdate);
    }

    private PreparedStatement createPreparedStatementForUpdate(Connection connection, LegalEntityClientInformation legalEntityClientInformation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_LEGAL_ENTITY_CLIENT_INFORMATION);
        statement.setString(1, legalEntityClientInformation.getName());
        statement.setString(2, legalEntityClientInformation.getAddress());
        statement.setString(3, legalEntityClientInformation.getPhoneNumber());
        statement.setString(4, legalEntityClientInformation.getCheckingAccount());
        statement.setString(5, legalEntityClientInformation.getPayerAccountNumber());
        return statement;
    }

    @Override
    public Optional<LegalEntityClientInformation> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createPreparedStatementForGettingById, this::createLegalEntityClientInformation);
    }

    private PreparedStatement createPreparedStatementForGettingById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
        statement.setLong(1, id);
        return statement;
    }


    private LegalEntityClientInformation createLegalEntityClientInformation(ResultSet resultSet) throws SQLException {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setLegalEntityClientInformationId(resultSet.getLong(LegalEntityClientInformationField.LEGAL_ENTITY_CLIENT_INFORMATION_ID.toString()));
        legalEntityClientInformation.setPayerAccountNumber(resultSet.getString(LegalEntityClientInformationField.PAYER_ACCOUNT_NUMBER.toString()));
        legalEntityClientInformation.setCheckingAccount(resultSet.getString(LegalEntityClientInformationField.CHECKING_ACCOUNT.toString()));
        legalEntityClientInformation.setName(resultSet.getString(LegalEntityClientInformationField.NAME.toString()));
        legalEntityClientInformation.setAddress(resultSet.getString(LegalEntityClientInformationField.ADDRESS.toString()));
        legalEntityClientInformation.setPhoneNumber(resultSet.getString(LegalEntityClientInformationField.PHONE_NUMBER.toString()));
        return legalEntityClientInformation;
    }
}
