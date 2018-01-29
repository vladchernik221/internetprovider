package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.RepositoryIntegrationTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class ServiceRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private ServiceRepository serviceRepository;

    public ServiceRepositoryIntegrationTest() {
        super("ServiceRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        serviceRepository = contextInitializer.getComponent(ServiceRepository.class);
    }


    private Service createTestExistService() {
        Service service = new Service();
        service.setServiceId(1L);
        service.setName("Service 1");
        service.setDescription("Description 1");
        service.setArchived(false);
        service.setPrice(new BigDecimal("12.22"));
        return service;
    }

    private Service createTestNewService() {
        Service service = new Service();
        service.setName("Service 100");
        service.setDescription("Description 100");
        service.setArchived(false);
        service.setPrice(new BigDecimal("142.34"));
        return service;
    }

    private Service createTestUpdatedService() {
        Service service = new Service();
        service.setServiceId(1L);
        service.setName("Service 100");
        service.setDescription("Description 100");
        service.setArchived(false);
        service.setPrice(new BigDecimal("155.56"));
        return service;
    }

    @Test
    public void existWithIdShouldReturnTrueWhenServiceExists() throws Exception {
        assertTrue(serviceRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenServiceDoesNotExist() throws Exception {
        assertFalse(serviceRepository.existWithId(100L));
    }

    @Test
    public void existWithNameShouldReturnTrueWhenServiceExists() throws Exception {
        assertTrue(serviceRepository.existWithName("Service 1"));
    }

    @Test
    public void existWithNameShouldReturnFalseWhenServiceDoesNotExist() throws Exception {
        assertFalse(serviceRepository.existWithName("Wrong name"));
    }

    @Test
    public void existWithIdAndNameShouldReturnFalseWhenServiceExists() throws Exception {
        assertFalse(serviceRepository.existWithNameAndNotId(1L, "Service 1"));
    }

    @Test
    public void existWithIdAndNameShouldReturnFalseWhenServiceWithNameDoesNotExist() throws Exception {
        assertFalse(serviceRepository.existWithNameAndNotId(1L, "Wrong name"));
    }

    @Test
    public void existWithIdAndNameShouldReturnTrueWhenServiceWithIdDoesNotExist() throws Exception {
        assertTrue(serviceRepository.existWithNameAndNotId(100L, "Service 1"));
    }

    @Test
    public void getByIdShouldReturnServiceWhenTariffPlanWithIdExists() throws Exception {
        Service expected = createTestExistService();
        Service actual = serviceRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenServiceWithIdDoesExist() throws Exception {
        assertFalse(serviceRepository.getById(100L).isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenServiceWithNameDoesNotExist() throws Exception {
        Long actual = serviceRepository.create(createTestNewService());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedService() throws Exception {
        Service expected = createTestNewService();
        Long generatedId = serviceRepository.create(expected);
        expected.setServiceId(generatedId);
        Service actual = serviceRepository.getById(generatedId).get();

        assertEquals(actual, expected);
    }

    @Test
    public void updateShouldSaveUpdatedService() throws Exception {
        Service expected = createTestUpdatedService();
        serviceRepository.update(expected);
        Service actual = serviceRepository.getById(expected.getServiceId()).get();
        assertEquals(actual, expected);
    }

    @Test
    public void archiveShouldArchiveTariffPlanWhenServiceNotArchived() throws Exception {
        serviceRepository.archive(1L);

        Service archivedTariffPlan = serviceRepository.getById(1L).get();

        assertTrue(archivedTariffPlan.getArchived());
    }

    @Test
    public void archiveShouldUnArchivedTariffPlanWhenServiceArchived() throws Exception {
        serviceRepository.archive(2L);

        Service archivedTariffPlan = serviceRepository.getById(2L).get();

        assertFalse(archivedTariffPlan.getArchived());
    }

    @Test
    public void getPageShouldReturnPageOfServiceWithArchived() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        Integer expectedPageCount = 1;
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        Page<Service> actual = serviceRepository.getPage(true, pageable);
        Integer actualPageCount = actual.getPagesCount();
        List<Long> actualIds = actual.getData().stream().map(Service::getServiceId).collect(Collectors.toList());

        assertEquals(actualPageCount, expectedPageCount);
        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnPageOfTariffPlansWithoutArchived() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        Integer expectedPageCount = 1;
        List<Long> expectedIds = Arrays.asList(1L, 3L);

        Page<Service> actual = serviceRepository.getPage(false, pageable);
        Integer actualPageCount = actual.getPagesCount();
        List<Long> actualIds = actual.getData().stream().map(Service::getServiceId).collect(Collectors.toList());

        assertEquals(actualPageCount, expectedPageCount);
        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<Service> actual = serviceRepository.getPage(true, pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void getPageByContractAnnexIdShouldReturnPageOfService() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        Page<Service> actual = serviceRepository.getPageByContractAnnexId(1L, pageable);
        List<Long> actualIds = actual.getData().stream().map(Service::getServiceId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageByContractAnnexIdShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<Service> actual = serviceRepository.getPageByContractAnnexId(1L, pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }
}
