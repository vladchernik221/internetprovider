package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entityfield.DiscountField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.DiscountRepository;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

@Repository
public class DiscountRepositoryImpl implements DiscountRepository {
    private static final Logger LOGGER = LogManager.getLogger(DiscountRepositoryImpl.class);


    private static final String CREATE_DISCOUNT = "INSERT INTO `discount`(`name`, `description`, `amount`, `start_date`, `end_date`, `only_for_new_client`) VALUES (?,?,?,?,?,?)";

    private static final String UPDATE_DISCOUNT = "UPDATE `discount` SET `name`=?, `description`=?, `amount`=?, `end_date`=?, `only_for_new_client`=? WHERE `discount_id`=?";

    private static final String GET_DISCOUNT_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `discount`";
                                                                                    //TODO it's not needed description for by page view
    private static final String GET_DISCOUNT_PAGE = "SELECT `discount_id`, `name`, `description`, `amount`, `start_date`, `end_date`, `only_for_new_client` FROM `discount` LIMIT ? OFFSET ?";

    private static final String GET_DISCOUNTS = "SELECT `discount_id`, `name` FROM `discount`";

    private static final String GET_DISCOUNT_BY_ID = "SELECT `discount_id`, `name`, `description`, `amount`, `start_date`, `end_date`, `only_for_new_client` FROM `discount` WHERE `discount_id`=?";

    private static final String REMOVE_DISCOUNT = "DELETE FROM `discount` WHERE `discount_id`=?";

    private static final String EXISTS_DISCOUNT_BY_ID = "SELECT EXISTS(SELECT 1 FROM `discount` WHERE `discount_id`=?)";

    private static final String EXISTS_DISCOUNT_BY_NAME = "SELECT EXISTS(SELECT 1 FROM `discount` WHERE `name`=?)";


    @Autowired
    private CommonRepository commonRepository;


    @Override
    public Long create(Discount discount) throws DatabaseException, TimeOutException {
        return commonRepository.create(discount, this::createStatementForInserting);
    }

    private PreparedStatement createStatementForInserting(Connection connection, Discount discount) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_DISCOUNT, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, discount.getName());
        statement.setObject(2, discount.getDescription(), Types.VARCHAR);
        statement.setInt(3, discount.getAmount());
        statement.setDate(4, discount.getStartDate());
        statement.setDate(5, discount.getEndDate());
        statement.setBoolean(6, discount.getOnlyForNewClient());
        return statement;
    }


    @Override
    public void update(Discount discount) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(discount, this::createPreparedStatementForUpdate);
    }

    private PreparedStatement createPreparedStatementForUpdate(Connection connection, Discount discount) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_DISCOUNT);
        statement.setString(1, discount.getName());
        statement.setObject(2, discount.getDescription(), Types.VARCHAR);
        statement.setInt(3, discount.getAmount());
        statement.setDate(4, discount.getEndDate());
        statement.setBoolean(5, discount.getOnlyForNewClient());
        statement.setLong(6, discount.getDiscountId());
        return statement;
    }


    @Override
    public Page<Discount> getPage(Pageable pageable) throws DatabaseException, TimeOutException {
        return commonRepository.getPage(pageable, this::createPreparedStatementForPageCount, this::createPreparedStatementForPage, this::createDiscount);
    }

    private PreparedStatement createPreparedStatementForPageCount(Connection connection, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_DISCOUNT_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        return statement;
    }

    private PreparedStatement createPreparedStatementForPage(Connection connection, Pageable pageable) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_DISCOUNT_PAGE);
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
        return statement;
    }

    @Override
    public List<Discount> getAll() throws DatabaseException, TimeOutException {
        return commonRepository.getAll(this::createPreparedStatementForGettingAll, this::createShortDiscount);
    }

    private PreparedStatement createPreparedStatementForGettingAll(Connection connection) throws SQLException {
        return connection.prepareStatement(GET_DISCOUNTS);
    }


    @Override
    public Optional<Discount> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createStatementForGetting, this::createDiscount);
    }

    private PreparedStatement createStatementForGetting(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_DISCOUNT_BY_ID);
        statement.setLong(1, id);
        return statement;
    }


    @Override
    public void remove(Long id) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(id, this::getPreparedStatementForRemove);
    }

    private PreparedStatement getPreparedStatementForRemove(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(REMOVE_DISCOUNT);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createStatementForExistById);
    }

    private PreparedStatement createStatementForExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_DISCOUNT_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public boolean existWithName(String name) throws DatabaseException, TimeOutException {
        return commonRepository.exist(name, this::createStatementForExistByName);
    }

    private PreparedStatement createStatementForExistByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_DISCOUNT_BY_NAME);
        statement.setString(1, name);
        return statement;
    }

    private Discount createShortDiscount(ResultSet resultSet) throws SQLException {
        Discount discount = new Discount();
        discount.setDiscountId(resultSet.getLong(DiscountField.DISCOUNT_ID.toString()));
        discount.setName(resultSet.getString(DiscountField.NAME.toString()));
        return discount;
    }

    private Discount createDiscount(ResultSet resultSet) throws SQLException {
        Discount discount = new Discount();
        discount.setDiscountId(resultSet.getLong(DiscountField.DISCOUNT_ID.toString()));
        discount.setName(resultSet.getString(DiscountField.NAME.toString()));
        discount.setDescription(resultSet.getString(DiscountField.DESCRIPTION.toString()));
        discount.setAmount(resultSet.getInt(DiscountField.AMOUNT.toString()));
        discount.setStartDate(resultSet.getDate(DiscountField.START_DATE.toString()));
        discount.setEndDate(resultSet.getDate(DiscountField.END_DATE.toString()));
        discount.setOnlyForNewClient(resultSet.getBoolean(DiscountField.ONLY_FOR_NEW_CLIENT.toString()));
        return discount;
    }
}
