package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.entityfield.IndividualClientInformationField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.IndividualClientInformationRepository;

import java.sql.*;
import java.util.Optional;

@Repository
public class IndividualClientInformationRepositoryImpl implements IndividualClientInformationRepository {

    private static final String CREATE_INDIVIDUAL_CLIENT_INFORMATION = "INSERT INTO `individual_client_information`(`first_name`, `second_name`, `last_name`, `passport_unique_identification`, `address`, `phone_number`) VALUES(?,?,?,?,?,?)";

    private static final String GET_BY_PASSPORT_DATA = "SELECT `individual_client_information_id`, `first_name`, `second_name`, `last_name`, `passport_unique_identification`, `address`, `phone_number` FROM `individual_client_information` WHERE `passport_unique_identification`=?";

    private static final String UPDATE_INDIVIDUAL_CLIENT_INFORMATION = "UPDATE `individual_client_information` SET  `first_name`=?, `second_name`=?, `last_name`=?, `address`=?, `phone_number`=? WHERE `passport_unique_identification`=?";

    private static final String GET_BY_ID = "SELECT `individual_client_information_id`, `first_name`, `second_name`, `last_name`, `passport_unique_identification`, `address`, `phone_number` FROM `individual_client_information` WHERE `individual_client_information_id`=?";


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
    public Optional<IndividualClientInformation> getByPassportData(String passportUniqueIdentification) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(passportUniqueIdentification, this::createPreparedStatementForGettingByPassportData, this::createIndividualClientInformation);
    }

    private PreparedStatement createPreparedStatementForGettingByPassportData(Connection connection, String passportUniqueIdentification) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_PASSPORT_DATA);
        statement.setString(1, passportUniqueIdentification);
        return statement;
    }

    private IndividualClientInformation createIndividualClientInformation(ResultSet resultSet) throws SQLException {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setIndividualClientInformationId(resultSet.getLong(IndividualClientInformationField.INDIVIDUAL_CLIENT_INFORMATION_ID.toString()));
        individualClientInformation.setPassportUniqueIdentification(resultSet.getString(IndividualClientInformationField.PASSPORT_UNIQUE_IDENTIFICATION.toString()));
        individualClientInformation.setFirstName(resultSet.getString(IndividualClientInformationField.FIRST_NAME.toString()));
        individualClientInformation.setSecondName(resultSet.getString(IndividualClientInformationField.SECOND_NAME.toString()));
        individualClientInformation.setLastName(resultSet.getString(IndividualClientInformationField.LAST_NAME.toString()));
        individualClientInformation.setAddress(resultSet.getString(IndividualClientInformationField.ADDRESS.toString()));
        String phoneNumber = (String) resultSet.getObject(IndividualClientInformationField.PHONE_NUMBER.toString());
        individualClientInformation.setPhoneNumber(phoneNumber);
        return individualClientInformation;
    }


    @Override
    public void update(IndividualClientInformation individualClientInformation) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(individualClientInformation, this::createStatementForUpdate);
    }

    private PreparedStatement createStatementForUpdate(Connection connection, IndividualClientInformation individualClientInformation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_INDIVIDUAL_CLIENT_INFORMATION);
        statement.setString(1, individualClientInformation.getFirstName());
        statement.setString(2, individualClientInformation.getSecondName());
        statement.setString(3, individualClientInformation.getLastName());
        statement.setString(4, individualClientInformation.getAddress());
        statement.setObject(5, individualClientInformation.getPhoneNumber(), Types.VARCHAR);
        statement.setString(6, individualClientInformation.getPassportUniqueIdentification());
        return statement;
    }


    @Override
    public Optional<IndividualClientInformation> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createPreparedStatementForGettingByID, this::createIndividualClientInformation);
    }


    private PreparedStatement createPreparedStatementForGettingByID(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
        statement.setLong(1, id);
        return statement;
    }
}
