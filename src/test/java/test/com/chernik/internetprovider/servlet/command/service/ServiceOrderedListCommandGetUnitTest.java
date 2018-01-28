package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceOrderedListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ServiceOrderedListCommandGetUnitTest extends CommandUnitTest {
    private ServiceOrderedListCommandGet command;
    private ServiceService serviceServiceMock;

    private Page<Service> servicesPage;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceOrderedListCommandGet();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        servicesPage = new Page<>(Arrays.asList(mock(Service.class), mock(Service.class)), 7);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/annex/5/service");
        when(requestMock.getParameter("page")).thenReturn("10");
        when(serviceServiceMock.getPageByContractAnnexId(anyLong(), any(Pageable.class))).thenReturn(servicesPage);
    }

    @Test
    public void executeShouldGetFirstServicesPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);

        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).getPageByContractAnnexId(5L, new Pageable(0, 10));
        verify(requestMock).setAttribute("servicesPage", servicesPage);
        verify(requestMock).setAttribute("supportArchived", false);
    }

    @Test
    public void executeShouldGetServicesPageWhenPageIsSpecified() throws Exception {
        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).getPageByContractAnnexId(5L,  new Pageable(9, 10));
        verify(requestMock).setAttribute("servicesPage", servicesPage);
        verify(requestMock).setAttribute("supportArchived", false);
    }
}
