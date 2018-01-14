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

import java.sql.*;
import java.util.Optional;

@Repository
public class ContractRepositoryImpl implements ContractRepository {

    private static final String CREATE_CONTRACT = "INSERT INTO `contract`(`dissolved`, `client_type`, `individual_client_information_id`, `legal_entity_client_information_id`) VALUES (?,?,?,?)";

    private static final String DISSOLVE_CONTRACT = "UPDATE `contract` SET `dissolved`=1 WHERE `contract_id`=?";

    private static final String GET_CONTRACT_BY_ID = "SELECT `contract_id`, `dissolved`, `client_type`, `legal_entity_client_information_id`, `individual_client_information_id` FROM `contract` WHERE `contract_id`=?";

    private static final String EXISTS_CONTRACT_BY_ID = "SELECT EXISTS(SELECT 1 FROM `contract` WHERE `contract_id`=?)";

    @Autowired
    private CommonRepository commonRepository;

    @Override
    public Long create(Contract contract) throws DatabaseException, TimeOutException {
        return commonRepository.create(contract, this::createPreparedStatementForInserting);
    }

    private PreparedStatement createPreparedStatementForInserting(Connection connection, Contract contract) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_CONTRACT, Statement.RETURN_GENERATED_KEYS);
        statement.setBoolean(1, contract.getDissolved());
        statement.setString(2, contract.getClientType().toString());
        if (contract.getIndividualClientInformation() != null) {
            statement.setLong(3, contract.getIndividualClientInformation().getIndividualClientInformationId());
        } else {
            statement.setNull(3, Types.INTEGER);
        }
        if (contract.getLegalEntityClientInformation() != null) {
            statement.setLong(4, contract.getLegalEntityClientInformation().getLegalEntityClientInformationId());
        } else {
            statement.setNull(4, Types.INTEGER);
        }
        return statement;
    }


    @Override
    public void dissolve(Long id) throws DatabaseException, TimeOutException {
        commonRepository.executeUpdate(id, this::createPreparedStatementForDissolved);
    }

    private PreparedStatement createPreparedStatementForDissolved(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DISSOLVE_CONTRACT);
        statement.setLong(1, id);
        return statement;
    }


    @Override
    public Optional<Contract> getById(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.getByParameters(id, this::createPreparedStatementForGetting, this::createContract);
    }

    private PreparedStatement createPreparedStatementForGetting(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_CONTRACT_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    private Contract createContract(ResultSet resultSet) throws SQLException {
        Contract contract = new Contract();
        contract.setContractId(resultSet.getLong(ContractField.CONTRACT_ID.toString()));
        contract.setDissolved(resultSet.getBoolean(ContractField.DISSOLVED.toString()));
        contract.setClientType(ClientType.valueOf(resultSet.getString(ContractField.CLIENT_TYPE.toString())));
        Long individualClientInformationId = (Long) resultSet.getObject(ContractField.INDIVIDUAL_CLIENT_INFORMATION_ID.toString());
        if (individualClientInformationId != null) {
            IndividualClientInformation individualClientInformation = new IndividualClientInformation();
            individualClientInformation.setIndividualClientInformationId(individualClientInformationId);
        }
        Long legalEntityClientInformationId = (Long) resultSet.getObject(ContractField.LEGAL_ENTITY_CLIENT_INFORMATION_ID.toString());
        if (legalEntityClientInformationId != null) {
            LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
            legalEntityClientInformation.setLegalEntityClientInformationId(legalEntityClientInformationId);
        }
        return contract;
    }


    @Override
    public boolean existsWithId(Long id) throws DatabaseException, TimeOutException {
        return commonRepository.exist(id, this::createStatementForContractExistsById);
    }

    private PreparedStatement createStatementForContractExistsById(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXISTS_CONTRACT_BY_ID);
        statement.setLong(1, id);
        return statement;
    }
}
