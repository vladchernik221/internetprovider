package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.entityfield.TariffPlanField;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TariffPlanRepositoryImpl implements TariffPlanRepository {
    private final static Logger LOGGER = LogManager.getLogger(TariffPlan.class);


    private final static String CREATE_TARIFF_PLAN = "INSERT INTO `tariff_plan`(`name`, `description`, `down_speed`, `up_speed`, `included_traffic`, `price_over_traffic`, `monthly_fee`, `archived`) VALUES(?,?,?,?,?,?,?,?)";

    private final static String IS_EXIST_TARIFF_PLAN_WITH_ID = "SELECT EXISTS(SELECT 1 FROM `tariff_plan` WHERE `tariff_plan_id`=?)";

    private final static String IS_EXIST_TARIFF_PLAN_WITH_NAME = "SELECT EXISTS(SELECT 1 FROM `tariff_plan` WHERE `name`=?)";

    private final static String UPDATE_TARIFF_PLAN = "UPDATE `tariff_plan` SET `name`=?, `description`=?, `down_speed`=?, `up_speed`=?, `included_traffic`=?, `price_over_traffic`=?, `monthly_fee`=?, `archived`=? WHERE `tariff_plan_id`=?";

    private final static String ARCHIVE_TARIFF_PLAN = "UPDATE `tariff_plan` SET `archived`=? WHERE `tariff_plan_id`=?";

    private final static String GET_TARIFF_PLANS_PAGE = "SELECT `tariff_plan_id`, `name`, `down_speed`, `up_speed`, `included_traffic`, `monthly_fee` FROM `tariff_plan` WHERE `archived`=0 LIMIT ? OFFSET ?";

    private final static String GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE = "SELECT `tariff_plan_id`, `name`, `down_speed`, `up_speed`, `included_traffic`, `monthly_fee` FROM `tariff_plan` LIMIT ? OFFSET ?";

    private final static String GET_TARIFF_PLANS_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `tariff_plan` WHERE `archived`=0";

    private final static String GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `tariff_plan`";

    private final static String GET_TARIFF_PLAN = "SELECT `tariff_plan_id`, `name`, `description`, `down_speed`, `up_speed`, `included_traffic`, `price_over_traffic`, `monthly_fee`, `archived` FROM `tariff_plan` WHERE `tariff_plan_id`=?";

    @Autowired
    private ConnectionPool connectionPool;


    @Override
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Inserting tariff plan {}", tariffPlan);
        Connection connection = connectionPool.getConnection();
        Long generatedId;
        try (PreparedStatement statement = createPreparedStatementForCreation(connection, tariffPlan)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating tariff plan failed, no rows affected.");
            }
            generatedId = getGeneratedId(statement);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when create tariff plan", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Inserting tariff plan complete successful");
        return generatedId;
    }

    private PreparedStatement createPreparedStatementForCreation(Connection connection, TariffPlan tariffPlan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_TARIFF_PLAN, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, tariffPlan.getName());
        String description = tariffPlan.getDescription();
        if (description != null) {
            statement.setString(2, description);
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        statement.setInt(3, tariffPlan.getDownSpeed());
        statement.setInt(4, tariffPlan.getUpSpeed());
        Integer includedTraffic = tariffPlan.getIncludedTraffic();
        if (includedTraffic != null) {
            statement.setInt(5, tariffPlan.getIncludedTraffic());
        } else {
            statement.setNull(5, Types.INTEGER);
        }
        Integer priceOverTraffic = tariffPlan.getPriceOverTraffic();
        if (includedTraffic != null) {
            statement.setInt(6, priceOverTraffic);
        } else {
            statement.setNull(6, Types.INTEGER);
        }
        statement.setBigDecimal(7, tariffPlan.getMonthlyFee());
        statement.setBoolean(8, tariffPlan.getArchived());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Updating tariff plan {}", tariffPlan);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForUpdating(connection, tariffPlan)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating tariff plan failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when update tariff plan", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Updating tariff plan complete successful");
    }

    private PreparedStatement createPreparedStatementForUpdating(Connection connection, TariffPlan tariffPlan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_TARIFF_PLAN);
        statement.setString(1, tariffPlan.getName());
        String description = tariffPlan.getDescription();
        if (description != null) {
            statement.setString(2, description);
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        statement.setInt(3, tariffPlan.getDownSpeed());
        statement.setInt(4, tariffPlan.getUpSpeed());
        Integer includedTraffic = tariffPlan.getIncludedTraffic();
        if (includedTraffic != null) {
            statement.setInt(5, tariffPlan.getIncludedTraffic());
        } else {
            statement.setNull(5, Types.INTEGER);
        }
        Integer priceOverTraffic = tariffPlan.getPriceOverTraffic();
        if (includedTraffic != null) {
            statement.setInt(6, priceOverTraffic);
        } else {
            statement.setNull(6, Types.INTEGER);
        }
        statement.setBigDecimal(7, tariffPlan.getMonthlyFee());
        statement.setBoolean(8, tariffPlan.getArchived());
        statement.setLong(9, tariffPlan.getTariffPlanId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void archive(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Archiving tariff plan. Set archive to {}", tariffPlan.getArchived());
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForArchived(connection, tariffPlan)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Archived tariff plan failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when archive tariff plan", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Archiving tariff plan complete successful");
    }

    private PreparedStatement createPreparedStatementForArchived(Connection connection, TariffPlan tariffPlan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ARCHIVE_TARIFF_PLAN);
        statement.setBoolean(1, tariffPlan.getArchived());
        statement.setLong(2, tariffPlan.getTariffPlanId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<TariffPlan> getTariffPlanPage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page of tariff plans. Page number is {}, page size is {}, contain archive {}", pageable.getPageNumber(), pageable.getPageSize(), archived);
        Connection connection = connectionPool.getConnection();
        Page<TariffPlan> tariffPlanPage = new Page<>();
        try (PreparedStatement countStatement = createCountStatement(connection, archived, pageable);
             ResultSet countResultSet = countStatement.executeQuery();
             PreparedStatement statement = createPreparedStatementForGetting(connection, archived, pageable);
             ResultSet resultSet = statement.executeQuery()) {

            if (countResultSet.next()) {
                tariffPlanPage.setPagesCount(countResultSet.getInt(1));
            } else {
                throw new DatabaseException("Getting tariff plan pages count failed");
            }

            List<TariffPlan> tariffPlans = new ArrayList<>(pageable.getPageSize());
            while (resultSet.next()) {
                tariffPlans.add(createShortTariffPlan(resultSet));
            }

            tariffPlanPage.setData(tariffPlans);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get tariff plans page", e);
        }
        LOGGER.log(Level.TRACE, "Getting tariff plans complete successful");
        return tariffPlanPage;
    }

    private PreparedStatement createCountStatement(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        LOGGER.log(Level.TRACE, "Create statement for tariff plan page count");
        PreparedStatement statement;
        statement = archived ? connection.prepareStatement(GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE_COUNT) :
                connection.prepareStatement(GET_TARIFF_PLANS_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement);
        return statement;
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        LOGGER.log(Level.TRACE, "Create statement for tariff plan page");
        PreparedStatement statement;
        statement = archived ? connection.prepareStatement(GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE) :
                connection.prepareStatement(GET_TARIFF_PLANS_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Optional<TariffPlan> getById(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting tariff plan by id {}", id);
        Connection connection = connectionPool.getConnection();
        TariffPlan tariffPlan = null;
        try (PreparedStatement statement = createPreparedStatementForGettingOne(connection, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                tariffPlan = createFullTariffPlan(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get tariff plan by id", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting tariff plan complete successful");
        return Optional.ofNullable(tariffPlan);
    }

    private PreparedStatement createPreparedStatementForGettingOne(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TARIFF_PLAN);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isTariffPlanWithNameExist(String name) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check tariff plan existing by name {}", name);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForExistByName(connection, name);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query, when check tariff plan with name exist");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when check tariff plan with name exist", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    private PreparedStatement createPreparedStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_NAME);
        statement.setString(1, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isTariffPlanWithIdExist(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check tariff plan existing by id {}", id);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForExistById(connection, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query, when check tariff plan with id exist");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when check tariff plan with id exist", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_ID);
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
                throw new DatabaseException("Getting tariff plan generated key failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get tariff plan generated key", e);
        }
        LOGGER.log(Level.TRACE, "Generated id: {}", generatedId);
        return generatedId;
    }

    private TariffPlan createShortTariffPlan(ResultSet resultSet) throws SQLException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(resultSet.getLong(TariffPlanField.TARIFF_PLAN_ID.toString()));
        tariffPlan.setName(resultSet.getString(TariffPlanField.NAME.toString()));
        tariffPlan.setDownSpeed(resultSet.getInt(TariffPlanField.DOWN_SPEED.toString()));
        tariffPlan.setUpSpeed(resultSet.getInt(TariffPlanField.UP_SPEED.toString()));
        tariffPlan.setIncludedTraffic(resultSet.getInt(TariffPlanField.INCLUDED_TRAFFIC.toString()));
        tariffPlan.setMonthlyFee(resultSet.getBigDecimal(TariffPlanField.MONTHLY_FEE.toString()));
        return tariffPlan;
    }

    private TariffPlan createFullTariffPlan(ResultSet resultSet) throws SQLException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(resultSet.getLong(TariffPlanField.TARIFF_PLAN_ID.toString()));
        tariffPlan.setName(resultSet.getString(TariffPlanField.NAME.toString()));
        tariffPlan.setDescription(resultSet.getString(TariffPlanField.DESCRIPTION.toString()));
        tariffPlan.setDownSpeed(resultSet.getInt(TariffPlanField.DOWN_SPEED.toString()));
        tariffPlan.setUpSpeed(resultSet.getInt(TariffPlanField.UP_SPEED.toString()));
        tariffPlan.setIncludedTraffic(resultSet.getInt(TariffPlanField.INCLUDED_TRAFFIC.toString()));
        tariffPlan.setPriceOverTraffic(resultSet.getInt(TariffPlanField.PRICE_OVER_TRAFFIC.toString()));
        tariffPlan.setMonthlyFee(resultSet.getBigDecimal(TariffPlanField.MONTHLY_FEE.toString()));
        tariffPlan.setArchived(resultSet.getBoolean(TariffPlanField.ARCHIVED.toString()));
        return tariffPlan;
    }
}
