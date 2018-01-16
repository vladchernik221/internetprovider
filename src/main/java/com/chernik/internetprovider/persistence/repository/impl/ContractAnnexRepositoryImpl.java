package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
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

    private static final String GET_CONTRACT_ANNEX_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `contract_annex` WHERE `contract_id`=?";

    private static final String GET_CONTRACT_ANNEX_PAGE = "SELECT `contract_annex_id`, `address`, `conclude_date`, `canceled`, `tariff_plan_id`, `contract_id` FROM `contract_annex` WHERE `contract_id`=? LIMIT ? OFFSET ?";

    private static final String GET_CONTRACT_ANNEX_BY_ID = "SELECT `contract_annex_id`, `address`, `conclude_date`, `canceled`, `tariff_plan_id`, `contract_id` FROM `contract_annex` WHERE `contract_annex_id`=?";

    private static final String EXISTS_CONTRACT_ANNEX_BY_ID = "SELECT EXISTS(SELECT 1 FROM `contract_annex` WHERE `contract_annex_id`=?)";

    private static final String CANCEL_CONTRACT_ANNEX = "UPDATE `contract_annex` SET `canceled`=1 WHERE `contract_annex_id`=?";

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
    public Page<ContractAnnex> getPage(Long contractId, Pageable pageable) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page of contract annexes for contract with ID {}. Page number is {}, page size is {}.", contractId, pageable.getPageNumber(), pageable.getPageSize());
        return commonRepository.getPage(contractId, pageable, this::createCountStatement, this::createPreparedStatementForGetting, this::createContractAnnex);
    }

    private PreparedStatement createCountStatement(Connection connection, Long contractId, Pageable pageable) throws SQLException {
        LOGGER.log(Level.TRACE, "Create statement for contract annexes page count");
        PreparedStatement statement = connection.prepareStatement(GET_CONTRACT_ANNEX_PAGE_COUNT);
        statement.setInt(1, pageable.getPageSize());
        statement.setLong(2, contractId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement);
        return statement;
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Long contractId, Pageable pageable) throws SQLException {
        LOGGER.log(Level.TRACE, "Create statement for contract annexes page");
        PreparedStatement statement = connection.prepareStatement(GET_CONTRACT_ANNEX_PAGE);
        statement.setLong(1, contractId);
        statement.setInt(2, pageable.getPageSize());
        statement.setInt(3, pageable.getPageNumber() * pageable.getPageSize());
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

    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createStatementForContractAnnexExistsById);
    }

    private PreparedStatement createStatementForContractAnnexExistsById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_CONTRACT_ANNEX_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public void cancel(Long id) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(id, this::createPreparedStatementForCanceling);
    }

    private PreparedStatement createPreparedStatementForCanceling(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CANCEL_CONTRACT_ANNEX);
        statement.setLong(1, id);
        return statement;
    }
}
