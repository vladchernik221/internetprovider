package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.context.Transactional;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.*;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.IndividualClientInformationService;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;
import com.chernik.internetprovider.service.UserService;

import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private IndividualClientInformationService individualClientInformationService;

    @Autowired
    private LegalEntityClientInformationService legalEntityClientInformationService;

    @Autowired
    private UserService userService;

    public void setContractRepository(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public void setIndividualClientInformationService(IndividualClientInformationService individualClientInformationService) {
        this.individualClientInformationService = individualClientInformationService;
    }

    public void setLegalEntityClientInformationService(LegalEntityClientInformationService legalEntityClientInformationService) {
        this.legalEntityClientInformationService = legalEntityClientInformationService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Override
    @Transactional
    public Long create(Contract contract, String userPassword) throws DatabaseException, TimeOutException, UnableSaveEntityException {
        if (contractRepository.existNotDissolvedByClientInformation(contract)) {
            throw new UnableSaveEntityException("Current client already has not dissolved contract");
        }

        Long clientInformationId;
        switch (contract.getClientType()) {
            case INDIVIDUAL:
                IndividualClientInformation individualClientInformation = contract.getIndividualClientInformation();
                clientInformationId = individualClientInformationService.createOrUpdate(individualClientInformation);
                individualClientInformation.setIndividualClientInformationId(clientInformationId);
                break;
            case LEGAL:
                LegalEntityClientInformation legalEntityClientInformation = contract.getLegalEntityClientInformation();
                clientInformationId = legalEntityClientInformationService.createOrUpdate(legalEntityClientInformation);
                legalEntityClientInformation.setLegalEntityClientInformationId(clientInformationId);
                break;
        }

        Long generatedId = contractRepository.create(contract);
        contract.setContractId(generatedId);

        User user = new User();
        user.setLogin(String.format("%06d", generatedId));
        user.setPassword(userPassword);
        user.setUserRole(UserRole.CUSTOMER);
        user.setContract(contract);
        userService.create(user);

        return generatedId;
    }

    @Override
    public Contract getById(Long id) throws DatabaseException, TimeOutException {
        Optional<Contract> contractOptional = contractRepository.getById(id);
        Contract contract = null;

        if (contractOptional.isPresent()) {
            contract = contractOptional.get();

            switch (contract.getClientType()) {
                case INDIVIDUAL:
                    IndividualClientInformation individualClientInformation = individualClientInformationService.getById(contract.getIndividualClientInformation().getIndividualClientInformationId());
                    contract.setIndividualClientInformation(individualClientInformation);
                    break;
                case LEGAL:
                    LegalEntityClientInformation legalEntityClientInformation = legalEntityClientInformationService.getById(contract.getLegalEntityClientInformation().getLegalEntityClientInformationId());
                    contract.setLegalEntityClientInformation(legalEntityClientInformation);
                    break;
            }
        }

        return contract;
    }

    @Override
    public Contract getByIdOrThrow(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException {
        Contract contract = getById(id);
        if (contract == null) {
            throw new EntityNotFoundException(String.format("Contract with id %s does not exist", id));
        }
        return contract;
    }

    @Override
    public void dissolve(Long id) throws DatabaseException, TimeOutException, EntityNotFoundException, UnableSaveEntityException {
        if (notExistById(id)) {
            throw new EntityNotFoundException(String.format("Contract with ID %d was not found", id));
        }
        if (contractRepository.isDissolved(id)) {
            throw new UnableSaveEntityException(String.format("Contract with id=%d already dissolved", id));
        }
        if (contractRepository.hasNotCanceledContractAnnex(id)) {
            throw new UnableSaveEntityException(String.format("Contract with id=%d has not canceled contract annex", id));
        }

        contractRepository.dissolve(id);
    }

    @Override
    public boolean notExistById(Long id) throws DatabaseException, TimeOutException {
        return !contractRepository.existWithId(id);
    }
}
