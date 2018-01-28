package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.impl.ServiceServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ServiceServiceUnitTest {
    private ServiceServiceImpl serviceService;

    private ServiceRepository serviceRepositoryMock;
    private ContractAnnexService contractAnnexServiceMock;

    @BeforeClass
    public void init() {
        serviceService = new ServiceServiceImpl();
        serviceRepositoryMock = mock(ServiceRepository.class);
        contractAnnexServiceMock = mock(ContractAnnexService.class);
        serviceService.setServiceRepository(serviceRepositoryMock);
        serviceService.setContractAnnexService(contractAnnexServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(serviceRepositoryMock, contractAnnexServiceMock);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void createShouldThrowExceptionWhenServiceWithNameAlreadyExists() throws Exception {
        when(serviceRepositoryMock.existWithName(anyString())).thenReturn(true);
        serviceService.create(createTestService());
    }

    @Test
    public void createShouldReturnGeneratedId() throws Exception {
        when(serviceRepositoryMock.existWithName(anyString())).thenReturn(false);
        when(serviceRepositoryMock.create(any(Service.class))).thenReturn(5L);

        Service service = createTestService();
        Long generatedId = serviceService.create(service);

        assertEquals(generatedId.longValue(), 5L);
        verify(serviceRepositoryMock).existWithName("test name");
        verify(serviceRepositoryMock).create(service);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void updateShouldThrowExceptionWhenServiceDoesNotExist() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(false);
        serviceService.update(createTestService());
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void updateShouldThrowExceptionWhenOtherServiceWithNameAlreadyExists() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(serviceRepositoryMock.existWithNameAndNotId(anyLong(), anyString())).thenReturn(true);
        serviceService.update(createTestService());
    }

    @Test
    public void updateShouldSaveUpdatedServiceInRepository() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(serviceRepositoryMock.existWithNameAndNotId(anyLong(), anyString())).thenReturn(false);

        Service service = createTestService();
        serviceService.update(service);

        verify(serviceRepositoryMock).existWithId(5L);
        verify(serviceRepositoryMock).existWithNameAndNotId(5L, "test name");
        verify(serviceRepositoryMock).update(service);
    }

    @Test
    public void getPageShouldReturnPageOfServices() throws Exception {
        Page<Service> page = new Page<>(Arrays.asList(mock(Service.class), mock(Service.class)), 5);
        when(serviceRepositoryMock.getPage(anyBoolean(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<Service> actualPage = serviceService.getPage(pageable, true);
        assertSame(actualPage, page);
        verify(serviceRepositoryMock).getPage(true, pageable);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionWhenServiceDoesNotExist() throws Exception {
        when(serviceRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());
        serviceService.getById(5L);
    }

    @Test
    public void getByIdShouldReturnServiceWhenServiceExists() throws Exception {
        Service service = createTestService();
        when(serviceRepositoryMock.getById(anyLong())).thenReturn(Optional.of(service));

        Service actualService = serviceService.getById(5L);
        assertSame(actualService, service);
        verify(serviceRepositoryMock).getById(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void archiveShouldThrowExceptionWhenServiceDoesNotExist() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(false);
        serviceService.archive(5L);
    }

    @Test
    public void archiveShouldChangeStatusWhenServiceExists() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(true);

        serviceService.archive(5L);
        verify(serviceRepositoryMock).existWithId(5L);
        verify(serviceRepositoryMock).archive(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getPageByContractAnnexIdShouldThrowExceptionWhenContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(false);
        serviceService.getPageByContractAnnexId(10L, new Pageable(3, 10));
    }

    @Test
    public void getPageByContractAnnexIdShouldPageOfServicesWhenContractAnnexExists() throws Exception {
        Page<Service> page = new Page<>(Arrays.asList(mock(Service.class), mock(Service.class)), 5);
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(true);
        when(serviceRepositoryMock.getPageByContractAnnexId(anyLong(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<Service> actualPage = serviceService.getPageByContractAnnexId(10L, pageable);

        assertSame(actualPage, page);
        verify(contractAnnexServiceMock).existById(10L);
        verify(serviceRepositoryMock).getPageByContractAnnexId(10L, pageable);
    }

    @Test
    public void existWithIdShouldReturnTrueWhenServiceExists() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(true);

        Boolean result = serviceService.existWithId(5L);
        assertTrue(result);
        verify(serviceRepositoryMock).existWithId(5L);
    }

    @Test
    public void existWithIdShouldReturnFalseWhenServiceDoesNotExist() throws Exception {
        when(serviceRepositoryMock.existWithId(anyLong())).thenReturn(false);

        Boolean result = serviceService.existWithId(5L);
        assertFalse(result);
        verify(serviceRepositoryMock).existWithId(5L);
    }

    private Service createTestService() {
        Service service = new Service(5L);
        service.setName("test name");
        return service;
    }
}
