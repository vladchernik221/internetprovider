package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.entityfield.TariffPlanField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
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
    private CommonRepository commonRepository;

    @Override
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        return commonRepository.create(tariffPlan, this::createPreparedStatementForCreation);
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
        commonRepository.update(tariffPlan, this::createPreparedStatementForUpdating);
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
        commonRepository.update(tariffPlan, this::createPreparedStatementForArchived);
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
        return commonRepository.getEntityPage(archived, pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createShortTariffPlan);
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
        return commonRepository.getByParameters(id, this::createPreparedStatementForGettingOne, this::createFullTariffPlan);
    }

    private PreparedStatement createPreparedStatementForGettingOne(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TARIFF_PLAN);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isTariffPlanWithNameExist(String name) throws DatabaseException, TimeOutException {
        return commonRepository.exist(name, this::createPreparedStatementForExistByName);
    }

    private PreparedStatement createPreparedStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_NAME);
        statement.setString(1, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isTariffPlanWithIdExist(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createPreparedStatementForExistById);
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
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
