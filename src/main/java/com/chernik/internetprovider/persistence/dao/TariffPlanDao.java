package com.chernik.internetprovider.persistence.dao;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TariffPlanDao implements TariffPlanRepository {
    private final static Logger LOGGER = LogManager.getLogger(TariffPlan.class);

    private final static String INSERT_TARIFF_PLAN =
            "INSERT INTO `tariff_plan`(`name`, `description`, `down_speed`, `up_speed`, `included_traffic`, `price_over_traffic`, `mounthly_fee`, `archived`) VALUE (?,?,?,?,?,?,?,?)";

    @Autowired
    private ConnectionPool connectionPool;

    @Override
    public Long create(TariffPlan tariffPlan) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Inserting tariff plan {}", tariffPlan);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = createPreparedStatementForCreation(connection, tariffPlan)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return null;
    }

    private PreparedStatement createPreparedStatementForCreation(Connection connection, TariffPlan tariffPlan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_TARIFF_PLAN);
        statement.setString(1, tariffPlan.getName());
        statement.setString(2, tariffPlan.getDescription());
        statement.setInt(3, tariffPlan.getDownSpeed());
        statement.setInt(4, tariffPlan.getUpSpeed());
        statement.setInt(5, tariffPlan.getIncludedTraffic());
        statement.setInt(6, tariffPlan.getPriceOverTraffic());
        statement.setBigDecimal(7, tariffPlan.getMonthlyFee());
        statement.setBoolean(8, tariffPlan.getArchived());
        return statement;
    }

    @Override
    public TariffPlan update(TariffPlan tariffPlan) {
        return null;
    }

    @Override
    public List<TariffPlan> getPage(Pageable pageable) {
        return null;
    }

    @Override
    public TariffPlan getById(Long id) {
        return null;
    }

    @Override
    public void archivedTariffPlan(TariffPlan tariffPlan) {

    }
}
