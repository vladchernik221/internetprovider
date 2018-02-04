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

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class TariffPlanRepositoryImpl implements TariffPlanRepository {
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanRepositoryImpl.class);


    private static final String CREATE_TARIFF_PLAN = "INSERT INTO `tariff_plan`(`name`, `description`, `down_speed`, `up_speed`, `included_traffic`, `price_over_traffic`, `monthly_fee`) VALUES(?,?,?,?,?,?,?)";

    private static final String IS_EXIST_TARIFF_PLAN_WITH_ID = "SELECT EXISTS(SELECT 1 FROM `tariff_plan` WHERE `tariff_plan_id`=?)";

    private static final String IS_EXIST_TARIFF_PLAN_WITH_NAME = "SELECT EXISTS(SELECT 1 FROM `tariff_plan` WHERE `name`=?)";

    private static final String IS_EXIST_TARIFF_PLAN_WITH_NAME_AND_NOT_ID = "SELECT EXISTS(SELECT 1 FROM `tariff_plan` WHERE `tariff_plan_id`!=? AND `name`=?)";

    private static final String UPDATE_TARIFF_PLAN = "UPDATE `tariff_plan` SET `name`=?, `description`=?, `down_speed`=?, `up_speed`=?, `included_traffic`=?, `price_over_traffic`=?, `monthly_fee`=? WHERE `tariff_plan_id`=?";

    private static final String ARCHIVE_TARIFF_PLAN = "UPDATE `tariff_plan` tp SET tp.archived=NOT tp.archived WHERE tp.tariff_plan_id=?";

    private static final String GET_TARIFF_PLANS_PAGE = "SELECT `tariff_plan_id`, `name`, `down_speed`, `up_speed`, `included_traffic`, `monthly_fee`, `archived` FROM `tariff_plan` WHERE `archived`=0 LIMIT ? OFFSET ?";

    private static final String GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE = "SELECT `tariff_plan_id`, `name`, `down_speed`, `up_speed`, `included_traffic`, `monthly_fee`, `archived` FROM `tariff_plan` LIMIT ? OFFSET ?";

    private static final String GET_TARIFF_PLANS_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `tariff_plan` WHERE `archived`=0";

    private static final String GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `tariff_plan`";

    private static final String GET_TARIFF_PLAN = "SELECT `tariff_plan_id`, `name`, `description`, `down_speed`, `up_speed`, `included_traffic`, `price_over_traffic`, `monthly_fee`, `archived` FROM `tariff_plan` WHERE `tariff_plan_id`=?";

    private static final String GET_ALL_NOT_ARCHIVED = "SELECT `tariff_plan_id`, `name`, `down_speed`, `up_speed`, `included_traffic`, `monthly_fee`, `archived` FROM `tariff_plan` WHERE `archived`=0";


    @Autowired
    private CommonRepository commonRepository;

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }


    @Override
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Creating tariff plan: {}", tariffPlan);
        return commonRepository.create(tariffPlan, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, TariffPlan tariffPlan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_TARIFF_PLAN, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, tariffPlan.getName());
        statement.setObject(2, tariffPlan.getDescription(), Types.VARCHAR);
        statement.setInt(3, tariffPlan.getDownSpeed());
        statement.setInt(4, tariffPlan.getUpSpeed());
        statement.setObject(5, tariffPlan.getIncludedTraffic(), Types.INTEGER);
        statement.setObject(6, tariffPlan.getPriceOverTraffic(), Types.DECIMAL);
        statement.setBigDecimal(7, tariffPlan.getMonthlyFee());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void update(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Updating tariff plan: {}", tariffPlan);
        commonRepository.executeUpdate(tariffPlan, this::createPreparedStatementForUpdating);
    }

    private PreparedStatement createPreparedStatementForUpdating(Connection connection, TariffPlan tariffPlan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_TARIFF_PLAN);
        statement.setString(1, tariffPlan.getName());
        statement.setObject(2, tariffPlan.getDescription(), Types.VARCHAR);
        statement.setInt(3, tariffPlan.getDownSpeed());
        statement.setInt(4, tariffPlan.getUpSpeed());
        statement.setObject(5, tariffPlan.getIncludedTraffic(), Types.INTEGER);
        statement.setObject(6, tariffPlan.getPriceOverTraffic(), Types.DECIMAL);
        statement.setBigDecimal(7, tariffPlan.getMonthlyFee());
        statement.setLong(8, tariffPlan.getTariffPlanId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void archive(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Archiving tariff plan with ID {}", id);
        commonRepository.executeUpdate(id, this::createPreparedStatementForArchived);
    }

    private PreparedStatement createPreparedStatementForArchived(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ARCHIVE_TARIFF_PLAN);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Page<TariffPlan> getPage(boolean archived, Pageable pageable) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page of tariff plan with archived status {}. Page number is {}, page size is {}", archived, pageable.getPageNumber(), pageable.getPageSize());
        return commonRepository.getPage(archived, pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createShortTariffPlan);
    }

    private PreparedStatement createCountStatement(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        PreparedStatement statement;
        statement = archived ? connection.prepareStatement(GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE_COUNT) :
                connection.prepareStatement(GET_TARIFF_PLANS_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement);
        return statement;
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Boolean archived, Pageable pageable) throws SQLException {
        PreparedStatement statement;
        statement = archived ? connection.prepareStatement(GET_TARIFF_PLANS_WITH_ARCHIVED_PAGE) :
                connection.prepareStatement(GET_TARIFF_PLANS_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private TariffPlan createShortTariffPlan(ResultSet resultSet) throws SQLException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(resultSet.getLong(TariffPlanField.TARIFF_PLAN_ID.toString()));
        tariffPlan.setName(resultSet.getString(TariffPlanField.NAME.toString()));
        Integer downSpeed = (Integer) resultSet.getObject(TariffPlanField.DOWN_SPEED.toString());
        tariffPlan.setDownSpeed(downSpeed);
        Integer upSpeed = (Integer) resultSet.getObject(TariffPlanField.UP_SPEED.toString());
        tariffPlan.setUpSpeed(upSpeed);
        Integer includedTraffic = (Integer) resultSet.getObject(TariffPlanField.INCLUDED_TRAFFIC.toString());
        tariffPlan.setIncludedTraffic(includedTraffic);
        tariffPlan.setMonthlyFee(resultSet.getBigDecimal(TariffPlanField.MONTHLY_FEE.toString()));
        tariffPlan.setArchived(resultSet.getBoolean(TariffPlanField.ARCHIVED.toString()));
        return tariffPlan;
    }


    @Override
    public Optional<TariffPlan> getById(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting tariff plan with ID {}", id);
        return commonRepository.getByParameters(id, this::createPreparedStatementForGettingOne, this::createFullTariffPlan);
    }

    private PreparedStatement createPreparedStatementForGettingOne(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_TARIFF_PLAN);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private TariffPlan createFullTariffPlan(ResultSet resultSet) throws SQLException {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(resultSet.getLong(TariffPlanField.TARIFF_PLAN_ID.toString()));
        tariffPlan.setName(resultSet.getString(TariffPlanField.NAME.toString()));
        tariffPlan.setDescription(resultSet.getString(TariffPlanField.DESCRIPTION.toString()));
        Integer downSpeed = (Integer) resultSet.getObject(TariffPlanField.DOWN_SPEED.toString());
        tariffPlan.setDownSpeed(downSpeed);
        Integer upSpeed = (Integer) resultSet.getObject(TariffPlanField.UP_SPEED.toString());
        tariffPlan.setUpSpeed(upSpeed);
        Integer includedTraffic = (Integer) resultSet.getObject(TariffPlanField.INCLUDED_TRAFFIC.toString());
        tariffPlan.setIncludedTraffic(includedTraffic);
        BigDecimal priceOverTraffic = (BigDecimal) resultSet.getObject(TariffPlanField.PRICE_OVER_TRAFFIC.toString());
        tariffPlan.setPriceOverTraffic(priceOverTraffic);
        tariffPlan.setMonthlyFee(resultSet.getBigDecimal(TariffPlanField.MONTHLY_FEE.toString()));
        tariffPlan.setArchived(resultSet.getBoolean(TariffPlanField.ARCHIVED.toString()));
        return tariffPlan;
    }


    @Override
    public boolean existWithName(String name) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing of tariff plan with name {}", name);
        return commonRepository.exist(name, this::createPreparedStatementForExistByName);
    }

    private PreparedStatement createPreparedStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_NAME);
        statement.setString(1, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing of tariff plan with ID {}", id);
        return commonRepository.exist(id, this::createPreparedStatementForExistById);
    }

    private PreparedStatement createPreparedStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public List<TariffPlan> getAllNotArchived() throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting all not archived tariff plans");
        return commonRepository.getAll(this::createStatementForGetAll, this::createShortTariffPlan);
    }

    private PreparedStatement createStatementForGetAll(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_ALL_NOT_ARCHIVED);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean existWithNameAndNotId(Long id, String name) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check that tariff plan with name {} and ID not {} exists", name, id);
        return commonRepository.exist(id, name, this::createStatementForExistByIdAndName);
    }

    private PreparedStatement createStatementForExistByIdAndName(Connection connection, Long id, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_EXIST_TARIFF_PLAN_WITH_NAME_AND_NOT_ID);
        statement.setLong(1, id);
        statement.setString(2, name);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }
}
