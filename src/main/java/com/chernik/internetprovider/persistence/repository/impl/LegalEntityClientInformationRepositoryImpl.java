package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.LegalEntityClientInformationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class LegalEntityClientInformationRepositoryImpl implements LegalEntityClientInformationRepository {

    private static final String CREATE_LEGAL_ENTITY_CLIENT_INFORMATION = "INSERT INTO `legal_entity_client_information`(`payer_account_number`, `name`, `address`, `phone_number`) VALUES(?,?,?,?)";

    private static final String EXISTS_BY_PAYER_ACCOUNT_NUMBER = "SELECT EXISTS(SELECT 1 FROM `legal_entity_client_information` leci JOIN `contract` c ON leci.legal_entity_client_information_id = c.legal_entity_client_information_id AND c.dissolved = 0 AND  leci.payer_account_number=?)";


    @Autowired
    private CommonRepository commonRepository;

    @Override
    public Long create(LegalEntityClientInformation legalEntityClientInformation) throws DatabaseException, TimeOutException {
        return commonRepository.create(legalEntityClientInformation, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, LegalEntityClientInformation legalEntityClientInformation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_LEGAL_ENTITY_CLIENT_INFORMATION, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, legalEntityClientInformation.getPayerAccountNumber());
        statement.setString(2, legalEntityClientInformation.getName());
        statement.setString(3, legalEntityClientInformation.getAddress());
        statement.setString(4, legalEntityClientInformation.getPhoneNumber());
        return statement;
    }


    @Override
    public boolean existsByPayerAccountNumber(String payerAccountNumber) throws DatabaseException, TimeOutException {
        return commonRepository.exist(payerAccountNumber, this::createPreparedStatementForExistsByPayerAccountNumber);
    }

    private PreparedStatement createPreparedStatementForExistsByPayerAccountNumber(Connection connection, String payerAccountNumber) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_BY_PAYER_ACCOUNT_NUMBER);
        statement.setString(1, payerAccountNumber);
        return statement;
    }
}