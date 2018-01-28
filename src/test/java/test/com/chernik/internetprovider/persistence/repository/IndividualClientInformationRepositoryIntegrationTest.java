package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.repository.IndividualClientInformationRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.util.RepositoryIntegrationTest;

import static org.testng.Assert.*;

public class IndividualClientInformationRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private IndividualClientInformationRepository individualClientInformationRepository;

    public IndividualClientInformationRepositoryIntegrationTest() {
        super("IndividualClientInformationRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        individualClientInformationRepository = contextInitializer.getComponent(IndividualClientInformationRepository.class);
    }


    private IndividualClientInformation createTestExistIndividualClientInformation() {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setIndividualClientInformationId(1L);
        individualClientInformation.setFirstName("FirstName test 1");
        individualClientInformation.setSecondName("SecondName test 1");
        individualClientInformation.setLastName("LastName test 1");
        individualClientInformation.setPassportUniqueIdentification("PassportUnique test 1");
        individualClientInformation.setAddress("Address test 1");
        individualClientInformation.setPhoneNumber("PhoneNumber test 1");
        return individualClientInformation;
    }

    private IndividualClientInformation createTestNewIndividualClientInformation() {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setFirstName("FirstName test 2");
        individualClientInformation.setSecondName("SecondName test 2");
        individualClientInformation.setLastName("LastName test 2");
        individualClientInformation.setPassportUniqueIdentification("PassportUnique test 2");
        individualClientInformation.setAddress("Address test 2");
        individualClientInformation.setPhoneNumber("PhoneNumber test 2");
        return individualClientInformation;
    }

    private IndividualClientInformation createTestUpdatedIndividualClientInformation() {
        IndividualClientInformation individualClientInformation = new IndividualClientInformation();
        individualClientInformation.setIndividualClientInformationId(1L);
        individualClientInformation.setFirstName("FirstName test 2");
        individualClientInformation.setSecondName("SecondName test 2");
        individualClientInformation.setLastName("LastName test 2");
        individualClientInformation.setPassportUniqueIdentification("PassportUnique test 1");
        individualClientInformation.setAddress("Address test 2");
        individualClientInformation.setPhoneNumber("PhoneNumber test 2");
        return individualClientInformation;
    }

    @Test
    public void getByIdShouldReturnIndividualClientInformationWhenIndividualClientInformationWithIdExists() throws Exception {
        IndividualClientInformation expected = createTestExistIndividualClientInformation();
        IndividualClientInformation actual = individualClientInformationRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenIndividualClientInformationWithIdDoesExist() throws Exception {
        assertFalse(individualClientInformationRepository.getById(100L).isPresent());
    }

    @Test
    public void getByPassportDataShouldReturnIndividualClientInformationWhenIndividualClientInformationWithIdExists() throws Exception {
        IndividualClientInformation expected = createTestExistIndividualClientInformation();
        IndividualClientInformation actual = individualClientInformationRepository.getByPassportData("PassportUnique test 1").get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByPassportDataShouldReturnNullWhenIndividualClientInformationWithIdDoesExist() throws Exception {
        assertFalse(individualClientInformationRepository.getByPassportData("Wrong passport data").isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenIndividualClientInformationWithNameDoesNotExist() throws Exception {
        Long actual = individualClientInformationRepository.create(createTestNewIndividualClientInformation());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedIndividualClientInformation() throws Exception {
        IndividualClientInformation expected = createTestNewIndividualClientInformation();
        Long generatedId = individualClientInformationRepository.create(expected);
        expected.setIndividualClientInformationId(generatedId);
        IndividualClientInformation actual = individualClientInformationRepository.getById(generatedId).get();

        assertEquals(actual, expected);
    }


    @Test
    public void updateShouldSaveUpdatedIndividualClientInformation() throws Exception {
        IndividualClientInformation expected = createTestUpdatedIndividualClientInformation();
        individualClientInformationRepository.update(expected);
        IndividualClientInformation actual = individualClientInformationRepository.getByPassportData(expected.getPassportUniqueIdentification()).get();
        assertEquals(actual, expected);
    }
}
