package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.persistence.repository.ContractAnnexHasServiceRepository;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.service.impl.ContractAnnexHasServiceServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ContractAnnexHasServiceServiceUnitTest {
    private ContractAnnexHasServiceServiceImpl contractAnnexHasServiceService;

    private ContractAnnexHasServiceRepository contractAnnexHasServiceRepositoryMock;
    private ContractAnnexService contractAnnexServiceMock;
    private ServiceService serviceServiceMock;

    @BeforeClass
    public void init() {
        contractAnnexHasServiceService = new ContractAnnexHasServiceServiceImpl();
        contractAnnexHasServiceRepositoryMock = mock(ContractAnnexHasServiceRepository.class);
        contractAnnexServiceMock = mock(ContractAnnexService.class);
        serviceServiceMock = mock(ServiceService.class);
        contractAnnexHasServiceService.setContractAnnexHasServiceRepository(contractAnnexHasServiceRepositoryMock);
        contractAnnexHasServiceService.setContractAnnexService(contractAnnexServiceMock);
        contractAnnexHasServiceService.setServiceService(serviceServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(contractAnnexServiceMock, serviceServiceMock);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void createShouldThrowExceptionWhenContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(false);

        contractAnnexHasServiceService.create(5L, 15L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void createShouldThrowExceptionWhenServiceDoesNotExist() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(true);
        when(serviceServiceMock.existWithId(anyLong())).thenReturn(false);

        contractAnnexHasServiceService.create(5L, 15L);
    }

    @Test
    public void createShouldAddServiceToContractAnnex() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(true);
        when(serviceServiceMock.existWithId(anyLong())).thenReturn(true);

        contractAnnexHasServiceService.create(5L, 15L);
        verify(contractAnnexHasServiceRepositoryMock).create(5L, 15L);
    }
}
