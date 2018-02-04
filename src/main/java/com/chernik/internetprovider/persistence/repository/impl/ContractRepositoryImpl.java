package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.entity.ClientType;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.entityfield.ContractField;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

@Repository
public class ContractRepositoryImpl implements ContractRepository {
    private static final Logger LOGGER = LogManager.getLogger(ContractRepositoryImpl.class);


    private static final String CREATE_CONTRACT = "INSERT INTO `contract`(`client_type`, `individual_client_information_id`, `legal_entity_client_information_id`) VALUES (?,?,?)";

    private static final String DISSOLVE_CONTRACT = "UPDATE `contract` SET `dissolved`=1, `dissolve_date`=CURRENT_TIMESTAMP WHERE `contract_id`=?";

    private static final String GET_CONTRACT_BY_ID = "SELECT `contract_id`, `conclude_date`, `dissolve_date`, `dissolved`, `client_type`, `legal_entity_client_information_id`, `individual_client_information_id` FROM `contract` WHERE `contract_id`=?";

    private static final String EXISTS_CONTRACT_BY_ID = "SELECT EXISTS(SELECT 1 FROM `contract` WHERE `contract_id`=?)";

    private static final String EXISTS_NOT_DISSOLVED_BY_CLIENT_INFORMATION = "SELECT EXISTS(SELECT 1 FROM `contract` c LEFT JOIN `individual_client_information` ici ON c.individual_client_information_id=ici.individual_client_information_id LEFT JOIN `legal_entity_client_information` leci ON c.legal_entity_client_information_id = leci.legal_entity_client_information_id WHERE `dissolved`=0 AND (ici.passport_unique_identification=? OR leci.payer_account_number=?))";

    private static final String IS_DISSOLVED = "SELECT EXISTS(SELECT 1 FROM `contract` WHERE `contract_id`=? AND `dissolved`=1)";

    private static final String HAS_NOT_CANCELED_CONTRACT_ANNEX = "SELECT EXISTS(SELECT 1 FROM `contract_annex` WHERE `contract_id`=? AND `canceled`=0)";

    private static final String IS_USER_OWNER = "SELECT EXISTS(SELECT 1 FROM `contract` c JOIN `user` u ON c.contract_id = u.contract_id WHERE c.contract_id=? AND u.user_id=?)";


    @Autowired
    private CommonRepository commonRepository;

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public Long create(Contract contract) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Creating contract: {}", contract);
        return commonRepository.create(contract, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, Contract contract) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_CONTRACT, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, contract.getClientType().toString());
        if (contract.getIndividualClientInformation() != null) {
            statement.setLong(2, contract.getIndividualClientInformation().getIndividualClientInformationId());
        } else {
            statement.setNull(2, Types.INTEGER);
        }
        if (contract.getLegalEntityClientInformation() != null) {
            statement.setLong(3, contract.getLegalEntityClientInformation().getLegalEntityClientInformationId());
        } else {
            statement.setNull(3, Types.INTEGER);
        }
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public void dissolve(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Dissolving of contract with ID {}", id);
        commonRepository.executeUpdate(id, this::createPreparedStatementForDissolved);
    }

    private PreparedStatement createPreparedStatementForDissolved(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DISSOLVE_CONTRACT);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public Optional<Contract> getById(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting contract with ID {}", id);
        return commonRepository.getByParameters(id, this::createPreparedStatementForGetting, this::createContract);
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_CONTRACT_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    private Contract createContract(ResultSet resultSet) throws SQLException {
        Contract contract = new Contract();
        contract.setContractId(resultSet.getLong(ContractField.CONTRACT_ID.toString()));
        contract.setConcludeDate(resultSet.getDate(ContractField.CONCLUDE_DATE.toString()));
        contract.setDissolveDate(resultSet.getDate(ContractField.DISSOLVE_DATE.toString()));
        contract.setDissolved(resultSet.getBoolean(ContractField.DISSOLVED.toString()));
        contract.setClientType(ClientType.valueOf(resultSet.getString(ContractField.CLIENT_TYPE.toString())));
        Long individualClientInformationId = (Long) resultSet.getObject(ContractField.INDIVIDUAL_CLIENT_INFORMATION_ID.toString());
        if (individualClientInformationId != null) {
            contract.setIndividualClientInformation(new IndividualClientInformation(individualClientInformationId));
        }
        Long legalEntityClientInformationId = (Long) resultSet.getObject(ContractField.LEGAL_ENTITY_CLIENT_INFORMATION_ID.toString());
        if (legalEntityClientInformationId != null) {
            contract.setLegalEntityClientInformation(new LegalEntityClientInformation(legalEntityClientInformationId));
        }
        return contract;
    }


    @Override
    public boolean existWithId(Long id) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing of contract with ID {}", id);
        return commonRepository.exist(id, this::createStatementForContractExistById);
    }

    private PreparedStatement createStatementForContractExistById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_CONTRACT_BY_ID);
        statement.setLong(1, id);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    @Override
    public boolean existNotDissolvedByClientInformation(Contract contract) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check that client doesn't have not dissolved contracts: {}", contract);
        return commonRepository.exist(contract, this::createPreparedStatementForExistNotDissolvedByClientInformation);
    }

    private PreparedStatement createPreparedStatementForExistNotDissolvedByClientInformation(Connection connection, Contract contract) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_NOT_DISSOLVED_BY_CLIENT_INFORMATION);
        IndividualClientInformation individualClientInformation = contract.getIndividualClientInformation();
        if (individualClientInformation != null) {
            statement.setString(1, individualClientInformation.getPassportUniqueIdentification());
        } else {
            statement.setNull(1, Types.VARCHAR);
        }
        LegalEntityClientInformation legalEntityClientInformation = contract.getLegalEntityClientInformation();
        if (legalEntityClientInformation != null) {
            statement.setString(2, legalEntityClientInformation.getPayerAccountNumber());
        } else {
            statement.setNull(2, Types.VARCHAR);
        }
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean isDissolved(Long contractId) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check that contract with ID {} is dissolved", contractId);
        return commonRepository.exist(contractId, this::createStatementForIsDissolved);
    }

    private PreparedStatement createStatementForIsDissolved(Connection connection, Long contractId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_DISSOLVED);
        statement.setLong(1, contractId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }


    @Override
    public boolean hasNotCanceledContractAnnex(Long contractId) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check that contract with ID {} has not canceled contract annexes", contractId);
        return commonRepository.exist(contractId, this::createStatementForHasCanceledContractAnnex);
    }

    private PreparedStatement createStatementForHasCanceledContractAnnex(Connection connection, Long contractId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(HAS_NOT_CANCELED_CONTRACT_ANNEX);
        statement.setLong(1, contractId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }

    @Override
    public boolean isUserOwner(Long contractId, Long userId) throws DatabaseException, TimeOutException {
        return commonRepository.exist(contractId, userId, this::createStatementForIsUserOwner);
    }

    private PreparedStatement createStatementForIsUserOwner(Connection connection, Long contractId, Long userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_USER_OWNER);
        statement.setLong(1, contractId);
        statement.setLong(2, userId);
        return statement;
    }
}
