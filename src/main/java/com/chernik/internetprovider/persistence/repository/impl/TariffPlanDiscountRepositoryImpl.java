package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.TariffPlanDiscountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TariffPlanDiscountRepositoryImpl implements TariffPlanDiscountRepository {

    private static final String CREATE = "INSERT INTO `tariff_plan_has_discount`(`tariff_plan_id`, `discount_id`) VALUES (?,?)";

    private static final String REMOVE = "DELETE FROM `tariff_plan_has_discount` WHERE `tariff_plan_id`=?";

    private static final String EXIST_BY_TARIFF_PLAN_ID = "SELECT EXISTS(SELECT COUNT(*) FROM `tariff_plan_has_discount` WHERE `tariff_plan_id`=?)";

    @Autowired
    private CommonRepository commonRepository;

    @Override
    public void create(Long tariffPlanId, List<Discount> discounts) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(tariffPlanId, discounts, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, Long tariffPlanId, List<Discount> discounts) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE);
        for (Discount discount : discounts) {
            statement.setLong(1, tariffPlanId);
            statement.setLong(2, discount.getDiscountId());
            statement.addBatch();
        }
        return statement;
    }


    @Override
    public void remove(Long tariffPlanId) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(tariffPlanId, this::createPreparedStatementForRemoving);
    }

    private PreparedStatement createPreparedStatementForRemoving(Connection connection, Long tariffPlanId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(REMOVE);
        statement.setLong(1, tariffPlanId);
        return statement;
    }


    @Override
    public boolean existByTariffPlanId(Long tariffPlanId) throws DatabaseException, TimeOutException {
        return commonRepository.exist(tariffPlanId, this::createPreparedStatementForExistingByTariffPlanId);
    }

    private PreparedStatement createPreparedStatementForExistingByTariffPlanId(Connection connection, Long tariffPlanId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXIST_BY_TARIFF_PLAN_ID);
        statement.setLong(1, tariffPlanId);
        return statement;
    }
}
