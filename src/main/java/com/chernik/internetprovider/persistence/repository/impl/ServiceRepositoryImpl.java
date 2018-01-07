package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.entityfield.ServiceField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {
    private final static Logger LOGGER = LogManager.getLogger(ServiceRepositoryImpl.class);


    private final static String CREATE_SERVICE = "INSERT INTO `service`(`name`, `description`, `price`, `archived`) VALUES(?,?,?,?)";

    private final static String UPDATE_SERVICE = "UPDATE `service` SET `name`=?, `description`=?, `price`=?, `archived`=? WHERE `service_id`=?";

    private final static String GET_SERVICE_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `service` WHERE `archived`=0";

    private final static String GET_SERVICE_PAGE = "SELECT `service_id`, `name`, `price` FROM `service` WHERE `archived`=0 LIMIT ? OFFSET ?";

    private final static String GET_SERVICE_WITH_ARCHIVED_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `service`";

    private final static String GET_SERVICE_WITH_ARCHIVED_PAGE = "SELECT `service_id`, `name`, `price` FROM `service` LIMIT ? OFFSET ?";

    private final static String GET_SERVICE_BY_ID = "SELECT `service_id`, `name`, `description`, `price`, `archived` FROM `service` WHERE `service_id`=?";

    private final static String ARCHIVE_SERVICE = "UPDATE `service` SET `archived`=? WHERE service_id=?";

    private final static String EXISTS_SERVICE_BY_NAME = "SELECT EXISTS(SELECT 1 FROM `service` WHERE `name`=?)";

    private final static String EXISTS_SERVICE_BY_ID = "SELECT EXISTS(SELECT 1 FROM `service` WHERE `service_id`=?)";


    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Long create(Service service) throws DatabaseException, TimeOutException {
        return commonRepository.create(service, this::createPreparedStatementForCreation);
    }

    private PreparedStatement createPreparedStatementForCreation(Connection connection, Service service) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_SERVICE, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, service.getName());
        String description = service.getDescription();
        if (description != null) {
            statement.setString(2, description);
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        statement.setBigDecimal(3, service.getPrice());
        statement.setBoolean(4, service.getArchived());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void update(Service service) throws DatabaseException, TimeOutException {
        commonRepository.update(service, this::createPreparedStatementForUpdating);
    }

    private PreparedStatement createPreparedStatementForUpdating(Connection connection, Service service) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_SERVICE);
        statement.setString(1, service.getName());
        String description = service.getDescription();
        if (description != null) {
            statement.setString(2, description);
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        statement.setBigDecimal(3, service.getPrice());
        statement.setBoolean(4, service.getArchived());
        statement.setLong(5, service.getServiceId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void archive(Service service) throws DatabaseException, TimeOutException {
        commonRepository.update(service, this::createPreparedStatementForArchived);
    }

    private PreparedStatement createPreparedStatementForArchived(Connection connection, Service service) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ARCHIVE_SERVICE);
        statement.setBoolean(1, service.getArchived());
        statement.setLong(2, service.getServiceId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<Service> getServicePage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page of services. Page number is {}, page size is {}, contain archive {}", pageable.getPageNumber(), pageable.getPageSize(), archived);
        return commonRepository.getEntityPage(archived, pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createShortService);
    }

    private PreparedStatement createCountStatement(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        LOGGER.log(Level.TRACE, "Create statement for service page count");
        PreparedStatement statement;
        statement = archived ? connection.prepareStatement(GET_SERVICE_WITH_ARCHIVED_PAGE_COUNT) :
                connection.prepareStatement(GET_SERVICE_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement);
        return statement;
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        LOGGER.log(Level.TRACE, "Create statement for service page");
        PreparedStatement statement;
        statement = archived ? connection.prepareStatement(GET_SERVICE_WITH_ARCHIVED_PAGE) :
                connection.prepareStatement(GET_SERVICE_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Optional<Service> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createPreparedStatementForGettingOne, this::createFullService);
    }

    private PreparedStatement createPreparedStatementForGettingOne(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_SERVICE_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isServiceWithNameExist(String name) throws DatabaseException, TimeOutException {
        return commonRepository.exist(name, this::createPreparedStatementForExistByName);
    }

    private PreparedStatement createPreparedStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_NAME);
        statement.setString(1, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isServiceWithIdExist(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createPreparedStatementForExistById);
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    private Service createShortService(ResultSet resultSet) throws SQLException {
        Service service = new Service();
        service.setServiceId(resultSet.getLong(ServiceField.SERVICE_ID.toString()));
        service.setName(resultSet.getString(ServiceField.NAME.toString()));
        service.setPrice(resultSet.getBigDecimal(ServiceField.PRICE.toString()));
        return service;
    }

    private Service createFullService(ResultSet resultSet) throws SQLException {
        Service service = new Service();
        service.setServiceId(resultSet.getLong(ServiceField.SERVICE_ID.toString()));
        service.setName(resultSet.getString(ServiceField.NAME.toString()));
        service.setDescription(resultSet.getString(ServiceField.DESCRIPTION.toString()));
        service.setPrice(resultSet.getBigDecimal(ServiceField.PRICE.toString()));
        service.setArchived(resultSet.getBoolean(ServiceField.ARCHIVED.toString()));
        return service;
    }
}
