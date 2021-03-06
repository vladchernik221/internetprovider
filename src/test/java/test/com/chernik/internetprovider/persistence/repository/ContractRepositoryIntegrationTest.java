package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.entity.*;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.RepositoryIntegrationTest;

import java.sql.Date;

import static org.testng.Assert.*;

public class ContractRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private ContractRepository contractRepository;

    public ContractRepositoryIntegrationTest() {
        super("ContractRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        contractRepository = contextInitializer.getComponent(ContractRepository.class);
    }

    private Contract createTestExistContract() {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setIndividualClientInformationId(1L);

        Contract contract = new Contract();
        contract.setContractId(1L);
        contract.setConcludeDate(Date.valueOf("2018-01-28"));
        contract.setDissolved(false);
        contract.setClientType(ClientType.INDIVIDUAL);
        contract.setIndividualClientInformation(individualClientInformation);
        return contract;
    }

    private Contract createTestNewContractWithIndividual() {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setIndividualClientInformationId(1L);

        Contract contract = new Contract();
        contract.setDissolved(false);
        contract.setClientType(ClientType.INDIVIDUAL);
        contract.setIndividualClientInformation(individualClientInformation);
        return contract;
    }

    private Contract createTestNewContractWithLegal() {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setLegalEntityClientInformationId(1L);

        Contract contract = new Contract();
        contract.setDissolved(false);
        contract.setClientType(ClientType.LEGAL);
        contract.setLegalEntityClientInformation(legalEntityClientInformation);
        return contract;
    }

    private Contract createTestContractWithIndividualClientInformation(String uniqueField) {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setPassportUniqueIdentification(uniqueField);
        Contract contract = new Contract();
        contract.setClientType(ClientType.INDIVIDUAL);
        contract.setIndividualClientInformation(individualClientInformation);
        return contract;
    }

    private Contract createTestContractWithLegalEntityClientInformation(String uniqueField) {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setPayerAccountNumber(uniqueField);
        Contract contract = new Contract();
        contract.setClientType(ClientType.LEGAL);
        contract.setLegalEntityClientInformation(legalEntityClientInformation);
        return contract;
    }


    @Test
    public void existWithIdShouldReturnTrueWhenContractExists() throws Exception {
        assertTrue(contractRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenContractDoesNotExist() throws Exception {
        assertFalse(contractRepository.existWithId(100L));
    }

    @Test
    public void isDissolvedShouldReturnTrueWhenContractDissolved() throws Exception {
        assertTrue(contractRepository.isDissolved(2L));
    }

    @Test
    public void isDissolvedWithIdShouldReturnFalseWhenContractDoesNotDissolved() throws Exception {
        assertFalse(contractRepository.isDissolved(1L));
    }

    @Test
    public void hasNotCanceledContractAnnexShouldReturnTrueWhenContractHasNotCanceledContractAnnex() throws Exception {
        assertTrue(contractRepository.hasNotCanceledContractAnnex(1L));
    }

    @Test
    public void hasNotCanceledContractAnnexShouldReturnFalseWhenContractHasOnlyCanceledContractAnnex() throws Exception {
        assertFalse(contractRepository.hasNotCanceledContractAnnex(3L));
    }

    @Test
    public void existNotDissolvedByIndividualClientInformationShouldReturnTrueWhenClientHasNotDissolvedContract() throws Exception {
        assertTrue(contractRepository.existNotDissolvedByClientInformation(createTestContractWithIndividualClientInformation("PassportUnique test 1")));
    }

    @Test
    public void existNotDissolvedByIndividualClientInformationShouldReturnFalseWhenClientHasOnlyDissolvedContract() throws Exception {
        assertFalse(contractRepository.existNotDissolvedByClientInformation(createTestContractWithIndividualClientInformation("PassportUnique test 2")));
    }

    @Test
    public void existNotDissolvedByLegalEntityClientInformationShouldReturnTrueWhenClientHasNotDissolvedContract() throws Exception {
        assertTrue(contractRepository.existNotDissolvedByClientInformation(createTestContractWithLegalEntityClientInformation("PayerAccountNumber test 1")));
    }

    @Test
    public void existNotDissolvedByLegalEntityClientInformationShouldReturnFalseWhenClientHasOnlyDissolvedContract() throws Exception {
        assertFalse(contractRepository.existNotDissolvedByClientInformation(createTestContractWithLegalEntityClientInformation("PayerAccountNumber test 2")));
    }

    @Test
    public void getByIdShouldReturnContractWhenContractWithIdExists() throws Exception {
        Contract expected = createTestExistContract();
        Contract actual = contractRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenContractWithIdDoesExist() throws Exception {
        assertFalse(contractRepository.getById(100L).isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenContractWithNameDoesNotExist() throws Exception {
        Long actual = contractRepository.create(createTestNewContractWithIndividual());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedContractWhenClientIsIndividual() throws Exception {
        Contract expected = createTestNewContractWithIndividual();
        Long generatedId = contractRepository.create(expected);
        expected.setContractId(generatedId);
        Contract actual = contractRepository.getById(generatedId).get();
        actual.setConcludeDate(null);

        assertEquals(actual, expected);
    }

    @Test
    public void createShouldSaveCreatedContractWhenClientIsLegal() throws Exception {
        Contract expected = createTestNewContractWithLegal();
        Long generatedId = contractRepository.create(expected);
        expected.setContractId(generatedId);
        Contract actual = contractRepository.getById(generatedId).get();
        actual.setConcludeDate(null);

        assertEquals(actual, expected);
    }

    @Test
    public void canceledShouldCancelContractAnnexWhenContractAnnexNotCanceled() throws Exception {
        contractRepository.dissolve(1L);

        Contract contract = contractRepository.getById(1L).get();

        assertTrue(contract.getDissolved());
    }

    @Test
    public void isUserOwnerShouldReturnTrueWhenUserIsOwnerOfContract() throws Exception {
        assertTrue(contractRepository.isUserOwner(1L, 1L));
    }

    @Test
    public void isUserOwnerWithIdShouldReturnFalseWhenUserIsNotOwnerOfContract() throws Exception {
        assertFalse(contractRepository.isUserOwner(1L, 100L));
    }
}
