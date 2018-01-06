package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.entityfield.ServiceField;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    private ConnectionPool connectionPool;


    @Override
    public Long create(Service service) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Inserting service {}", service);
        Connection connection = connectionPool.getConnection();
        Long generatedId;
        try (PreparedStatement statement = createPreparedStatementForCreation(connection, service)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating service failed, no rows affected.");
            }
            generatedId = getGeneratedId(statement);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when create service", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Inserting service complete successful");
        return generatedId;
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
        LOGGER.log(Level.TRACE, "Updating service {}", service);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForUpdating(connection, service)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating service failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when update service", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Updating service complete successful");
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
        LOGGER.log(Level.TRACE, "Archiving service. Set archive to {}", service.getArchived());
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForArchived(connection, service)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Archived service failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when archive service", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Archiving service complete successful");
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
        Connection connection = connectionPool.getConnection();
        Page<Service> servicePage = new Page<>();
        try (PreparedStatement countStatement = createCountStatement(connection, archived, pageable);
             ResultSet countResultSet = countStatement.executeQuery();
             PreparedStatement statement = createPreparedStatementForGetting(connection, archived, pageable);
             ResultSet resultSet = statement.executeQuery()) {

            if (countResultSet.next()) {
                servicePage.setPagesCount(countResultSet.getInt(1));
            } else {
                throw new DatabaseException("Getting services pages count failed");
            }

            List<Service> services = new ArrayList<>(pageable.getPageSize());
            while (resultSet.next()) {
                services.add(createShortService(resultSet));
            }

            servicePage.setData(services);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get services page", e);
        }
        LOGGER.log(Level.TRACE, "Getting services complete successful");
        return servicePage;
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
        LOGGER.log(Level.TRACE, "Getting service by id {}", id);
        Connection connection = connectionPool.getConnection();
        Service service = null;
        try (PreparedStatement statement = createPreparedStatementForGettingOne(connection, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                service = createFullService(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get service by id", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting service complete successful");
        return Optional.ofNullable(service);
    }

    private PreparedStatement createPreparedStatementForGettingOne(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_SERVICE_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isServiceWithNameExist(String name) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check service existing by name {}", name);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForExistByName(connection, name);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query, when check service with name exist");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when check service with name exist", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    private PreparedStatement createPreparedStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_NAME);
        statement.setString(1, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isServiceWithIdExist(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check service existing by id {}", id);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForExistById(connection, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query, when check service with id exist");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when check service with id exist", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_SERVICE_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    private Long getGeneratedId(PreparedStatement statement) throws DatabaseException {
        Long generatedId;
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getLong(1);
            } else {
                throw new DatabaseException("Getting service generated key failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get service generated key", e);
        }
        LOGGER.log(Level.TRACE, "Generated id: {}", generatedId);
        return generatedId;
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
