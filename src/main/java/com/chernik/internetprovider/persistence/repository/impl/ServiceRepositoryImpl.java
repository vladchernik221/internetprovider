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
    private static final Logger LOGGER = LogManager.getLogger(ServiceRepositoryImpl.class);


    private static final String CREATE_SERVICE = "INSERT INTO `service`(`name`, `description`, `price`) VALUES(?,?,?)";

    private static final String UPDATE_SERVICE = "UPDATE `service` SET `name`=?, `description`=?, `price`=? WHERE `service_id`=?";

    private static final String GET_SERVICE_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `service` WHERE `archived`=0";

    private static final String GET_SERVICE_PAGE = "SELECT `service_id`, `name`, `price`, `archived` FROM `service` WHERE `archived`=0 LIMIT ? OFFSET ?";

    private static final String GET_SERVICE_WITH_ARCHIVED_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `service`";

    private static final String GET_SERVICE_WITH_ARCHIVED_PAGE = "SELECT `service_id`, `name`, `price`, `archived` FROM `service` LIMIT ? OFFSET ?";

    private static final String GET_SERVICE_BY_ID = "SELECT `service_id`, `name`, `description`, `price`, `archived` FROM `service` WHERE `service_id`=?";

    private static final String ARCHIVE_SERVICE = "UPDATE `service` s SET s.archived=NOT s.archived WHERE s.service_id=?";

    private static final String EXISTS_SERVICE_BY_NAME = "SELECT EXISTS(SELECT 1 FROM `service` WHERE `name`=?)";

    private static final String EXISTS_SERVICE_BY_ID = "SELECT EXISTS(SELECT 1 FROM `service` WHERE `service_id`=?)";

    private static final String EXISTS_SERVICE_BY_NAME_AND_NOT_ID = "SELECT EXISTS(SELECT 1 FROM `service` WHERE `service_id`!=? AND `name`=?)";

    private static final String GET_BY_CONTRACT_ANNEX_ID_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `service` s JOIN `contract_annex_has_service` cahs ON cahs.service_id=s.service_id WHERE cahs.contract_annex_id=?";

    private static final String GET_BY_CONTRACT_ANNEX_ID_PAGE = "SELECT s.service_id, s.name, s.price, s.archived FROM `service` s JOIN `contract_annex_has_service` cahs ON cahs.service_id=s.service_id WHERE cahs.contract_annex_id=? LIMIT ? OFFSET ?";


    @Autowired
    private CommonRepository commonRepository;

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }


    @Override
    public Long create(Service service) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Creating service: {}", service);
        return commonRepository.create(service, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, Service service) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_SERVICE, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, service.getName());
        statement.setObject(2, service.getDescription(), Types.VARCHAR);
        statement.setBigDecimal(3, service.getPrice());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void update(Service service) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Updating service: {}", service);
        commonRepository.executeUpdate(service, this::createPreparedStatementForUpdating);
    }

    private PreparedStatement createPreparedStatementForUpdating(Connection connection, Service service) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_SERVICE);
        statement.setString(1, service.getName());
        statement.setObject(2, service.getDescription(), Types.VARCHAR);
        statement.setBigDecimal(3, service.getPrice());
        statement.setLong(4, service.getServiceId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void archive(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Archiving service with ID {}", id);
        commonRepository.executeUpdate(id, this::createPreparedStatementForArchived);
    }

    private PreparedStatement createPreparedStatementForArchived(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ARCHIVE_SERVICE);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<Service> getPage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page of services. Page number is {}, page size is {}, contain archive {}", pageable.getPageNumber(), pageable.getPageSize(), archived);
        return commonRepository.getPage(archived, pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createShortService);
    }

    private PreparedStatement createCountStatement(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        PreparedStatement statement = archived ? connection.prepareStatement(GET_SERVICE_WITH_ARCHIVED_PAGE_COUNT) :
                connection.prepareStatement(GET_SERVICE_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement);
        return statement;
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        PreparedStatement statement = archived ? connection.prepareStatement(GET_SERVICE_WITH_ARCHIVED_PAGE) :
                connection.prepareStatement(GET_SERVICE_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private Service createShortService(ResultSet resultSet) throws SQLException {
        Service service = new Service();
        service.setServiceId(resultSet.getLong(ServiceField.SERVICE_ID.toString()));
        service.setName(resultSet.getString(ServiceField.NAME.toString()));
        service.setPrice(resultSet.getBigDecimal(ServiceField.PRICE.toString()));
        service.setArchived(resultSet.getBoolean(ServiceField.ARCHIVED.toString()));
        return service;
    }


    @Override
    public Optional<Service> getById(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting service with ID {}", id);
        return commonRepository.getByParameters(id, this::createPreparedStatementForGettingOne, this::createFullService);
    }

    private PreparedStatement createPreparedStatementForGettingOne(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_SERVICE_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
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


    @Override
    public boolean existWithName(String name) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing of service with name {}", name);
        return commonRepository.exist(name, this::createPreparedStatementForExistByName);
    }

    private PreparedStatement createPreparedStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_NAME);
        statement.setString(1, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing of service with ID {}", id);
        return commonRepository.exist(id, this::createPreparedStatementForExistById);
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean existWithNameAndNotId(Long id, String name) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check that service with name {} and ID not {} exist", name, id);
        return commonRepository.exist(id, name, this::createStatementForExistByIdAndName);
    }

    private PreparedStatement createStatementForExistByIdAndName(Connection connection, Long id, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_NAME_AND_NOT_ID);
        statement.setLong(1, id);
        statement.setString(2, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<Service> getPageByContractAnnexId(Long id, Pageable pageable) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page of services for contract annex with ID {}. Page number is {}, page size is {}", id, pageable.getPageNumber(), pageable.getPageSize());
        return commonRepository.getPage(id, pageable, this::createStatementForGetPageCountByContractAnnexId, this::createStatementForGetPageByContractAnnexId, this::createShortService);
    }

    private PreparedStatement createStatementForGetPageCountByContractAnnexId(Connection connection, Long id, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_CONTRACT_ANNEX_ID_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        statement.setLong(2, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private PreparedStatement createStatementForGetPageByContractAnnexId(Connection connection, Long id, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_BY_CONTRACT_ANNEX_ID_PAGE);
        statement.setLong(1, id);
        statement.setInt(2, pageable.getPageSize());
        statement.setInt(3, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }
}
