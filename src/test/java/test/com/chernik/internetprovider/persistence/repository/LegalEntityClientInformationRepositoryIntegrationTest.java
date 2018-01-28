package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.repository.LegalEntityClientInformationRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.util.RepositoryIntegrationTest;

import static org.testng.Assert.*;

public class LegalEntityClientInformationRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private LegalEntityClientInformationRepository legalEntityClientInformationRepository;

    public LegalEntityClientInformationRepositoryIntegrationTest() {
        super("LegalEntityClientInformationRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        legalEntityClientInformationRepository = contextInitializer.getComponent(LegalEntityClientInformationRepository.class);
    }

    private LegalEntityClientInformation createTestExistLegalEntityClientInformation() {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setLegalEntityClientInformationId(1L);
        legalEntityClientInformation.setPayerAccountNumber("PayerAccountNumber test 1");
        legalEntityClientInformation.setCheckingAccount("CheckingAccount test 1");
        legalEntityClientInformation.setName("Name test 1");
        legalEntityClientInformation.setAddress("Address test 1");
        legalEntityClientInformation.setPhoneNumber("PhoneNumber test 1");
        return legalEntityClientInformation;
    }

    private LegalEntityClientInformation createTestNewLegalEntityClientInformation() {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setPayerAccountNumber("PayerAccountNumber test 2");
        legalEntityClientInformation.setCheckingAccount("CheckingAccount test 2");
        legalEntityClientInformation.setName("Name test 2");
        legalEntityClientInformation.setAddress("Address test 2");
        legalEntityClientInformation.setPhoneNumber("PhoneNumber test 2");
        return legalEntityClientInformation;
    }

    private LegalEntityClientInformation createTestUpdatedLegalEntityClientInformation() {
        LegalEntityClientInformation legalEntityClientInformation = new LegalEntityClientInformation();
        legalEntityClientInformation.setLegalEntityClientInformationId(1L);
        legalEntityClientInformation.setPayerAccountNumber("PayerAccountNumber test 1");
        legalEntityClientInformation.setCheckingAccount("CheckingAccount test 2");
        legalEntityClientInformation.setName("Name test 2");
        legalEntityClientInformation.setAddress("Address test 2");
        legalEntityClientInformation.setPhoneNumber("PhoneNumber test 2");
        return legalEntityClientInformation;
    }

    @Test
    public void getByIdShouldReturnIndividualClientInformationWhenIndividualClientInformationWithIdExists() throws Exception {
        LegalEntityClientInformation expected = createTestExistLegalEntityClientInformation();
        LegalEntityClientInformation actual = legalEntityClientInformationRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenIndividualClientInformationWithIdDoesExist() throws Exception {
        assertFalse(legalEntityClientInformationRepository.getById(100L).isPresent());
    }

    @Test
    public void getByPassportDataShouldReturnIndividualClientInformationWhenIndividualClientInformationWithIdExists() throws Exception {
        LegalEntityClientInformation expected = createTestExistLegalEntityClientInformation();
        LegalEntityClientInformation actual = legalEntityClientInformationRepository.getByPayerAccountNumber("PayerAccountNumber test 1").get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByPassportDataShouldReturnNullWhenIndividualClientInformationWithIdDoesExist() throws Exception {
        assertFalse(legalEntityClientInformationRepository.getByPayerAccountNumber("Wrong passport data").isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenIndividualClientInformationWithNameDoesNotExist() throws Exception {
        Long actual = legalEntityClientInformationRepository.create(createTestNewLegalEntityClientInformation());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedIndividualClientInformation() throws Exception {
        LegalEntityClientInformation expected = createTestNewLegalEntityClientInformation();
        Long generatedId = legalEntityClientInformationRepository.create(expected);
        expected.setLegalEntityClientInformationId(generatedId);
        LegalEntityClientInformation actual = legalEntityClientInformationRepository.getById(generatedId).get();

        assertEquals(actual, expected);
    }


    @Test
    public void updateShouldSaveUpdatedIndividualClientInformation() throws Exception {
        LegalEntityClientInformation expected = createTestUpdatedLegalEntityClientInformation();
        legalEntityClientInformationRepository.update(expected);
        LegalEntityClientInformation actual = legalEntityClientInformationRepository.getByPayerAccountNumber(expected.getPayerAccountNumber()).get();
        assertEquals(actual, expected);
    }
}
