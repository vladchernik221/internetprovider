package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.repository.CommonRepository;
import com.chernik.internetprovider.persistence.repository.ContractAnnexHasServiceRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ContractAnnexHasServiceRepositoryImpl implements ContractAnnexHasServiceRepository {
    private static final Logger LOGGER = LogManager.getLogger(ContractAnnexHasServiceRepositoryImpl.class);

    private static final String CREATE = "INSERT INTO `contract_annex_has_service`(`contract_annex_id`, `service_id`) VALUES (?,?)";

    @Autowired
    private CommonRepository commonRepository;

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public void create(Long contractAnnexId, Long serviceId) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Adding service with ID {} to contract annex with ID {}", serviceId, contractAnnexId);
        commonRepository.create(contractAnnexId, serviceId, this::createStatementForInsert);
    }

    private PreparedStatement createStatementForInsert(Connection connection, Long contractAnnexId, Long serviceId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, contractAnnexId);
        statement.setLong(2, serviceId);
        LOGGER.log(Level.TRACE, "Create statement with query: {}", statement.toString());
        return statement;
    }
}
