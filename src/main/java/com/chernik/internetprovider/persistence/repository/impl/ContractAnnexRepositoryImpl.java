package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.ContractAnnexRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ContractAnnexRepositoryImpl implements ContractAnnexRepository {
    private static final Logger LOGGER = LogManager.getLogger(ContractAnnexRepositoryImpl.class);

    private static final String CREATE_CONTRACT_ANNEX = "INSERT INTO `contract_annex`(`address`, `tariff_plan_id`, `contract_id`) VALUES(?,?,?)";

    @Autowired
    private CommonRepository commonRepository;

    @Override
    public Long create(ContractAnnex contractAnnex) throws DatabaseException, TimeOutException {
        return commonRepository.create(contractAnnex, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, ContractAnnex contractAnnex) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_CONTRACT_ANNEX, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, contractAnnex.getAddress());
        statement.setLong(2, contractAnnex.getTariffPlan().getTariffPlanId());
        statement.setLong(3, contractAnnex.getContract().getContractId());
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }
}
