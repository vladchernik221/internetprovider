package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.entityfield.ContractAnnexField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.ContractAnnexRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

@Repository
public class ContractAnnexRepositoryImpl implements ContractAnnexRepository {
    private static final Logger LOGGER = LogManager.getLogger(ContractAnnexRepositoryImpl.class);

    private static final String CREATE_CONTRACT_ANNEX = "INSERT INTO `contract_annex`(`address`, `tariff_plan_id`, `contract_id`) VALUES(?,?,?)";

    private static final String GET_CONTRACT_ANNEX_BY_ID = "SELECT `contract_annex_id`, `address`, `conclude_date`, `canceled`, `tariff_plan_id`, `contract_id` FROM `contract_annex` WHERE `contract_annex_id`=?";

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

    @Override
    public Optional<ContractAnnex> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createPreparedStatementForGetting, this::createContractAnnex);
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_CONTRACT_ANNEX_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    private ContractAnnex createContractAnnex(ResultSet resultSet) throws SQLException {
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(resultSet.getLong(ContractAnnexField.CONTRACT_ANNEX_ID.toString()));
        contractAnnex.setAddress(resultSet.getString(ContractAnnexField.ADDRESS.toString()));
        contractAnnex.setConcludeDate(resultSet.getDate(ContractAnnexField.CONCLUDE_DATE.toString()));
        contractAnnex.setCanceled(resultSet.getBoolean(ContractAnnexField.CANCELED.toString()));

        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(resultSet.getLong(ContractAnnexField.TARIFF_PLAN_ID.toString()));
        contractAnnex.setTariffPlan(tariffPlan);

        Contract contract = new Contract();
        contract.setContractId(resultSet.getLong(ContractAnnexField.CONTRACT_ID.toString()));
        contractAnnex.setContract(contract);
        return contractAnnex;
    }
}
