package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.IndividualClientInformationRepository;

import java.sql.*;

@Repository
public class IndividualClientInformationRepositoryImpl implements IndividualClientInformationRepository {

    private static final String CREATE_INDIVIDUAL_CLIENT_INFORMATION = "INSERT INTO `individual_client_information`(`first_name`, `second_name`, `last_name`, `passport_unique_identification`, `address`, `phone_number`) VALUES(?,?,?,?,?,?)";

    private static final String EXISTS_BY_PASSPORT_DATA = "SELECT EXISTS(SELECT 1 FROM `individual_client_information` ici JOIN `contract` c ON ici.individual_client_information_id = c.individual_client_information_id AND c.dissolved = 0 AND  ici.passport_unique_identification=?)";

    @Autowired
    private CommonRepository commonRepository;

    @Override
    public Long create(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException {
        return commonRepository.create(individualClientInformation, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, IndividualClientInformation individualClientInformation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_INDIVIDUAL_CLIENT_INFORMATION, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, individualClientInformation.getFirstName());
        statement.setString(2, individualClientInformation.getSecondName());
        statement.setString(3, individualClientInformation.getLastName());
        statement.setString(4, individualClientInformation.getPassportUniqueIdentification());
        statement.setString(5, individualClientInformation.getAddress());
        statement.setObject(6, individualClientInformation.getPhoneNumber(), Types.VARCHAR);
        return statement;
    }


    @Override
    public boolean existByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException {
        return commonRepository.exist(passportUniqueIdentification, this::createPreparedStatementForExistsByPassportData);
    }

    private PreparedStatement createPreparedStatementForExistsByPassportData(Connection connection, String passportUniqueIdentification) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_BY_PASSPORT_DATA);
        statement.setString(1, passportUniqueIdentification);
        return statement;
    }
}
