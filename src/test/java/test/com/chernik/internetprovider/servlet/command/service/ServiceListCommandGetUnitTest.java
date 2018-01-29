package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ServiceListCommandGetUnitTest extends CommandUnitTest {
    private ServiceListCommandGet command;
    private ServiceService serviceServiceMock;

    private Page<Service> servicesPage;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceListCommandGet();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        servicesPage = new Page<>(Arrays.asList(mock(Service.class), mock(Service.class)), 7);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock);
        super.resetMocks();
        when(serviceServiceMock.getPage(any(Pageable.class), anyBoolean())).thenReturn(servicesPage);
    }

    @Test
    public void executeShouldGetFirstServicesPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);
        when(requestMock.getParameter("archived")).thenReturn("false");

        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).getPage(new Pageable(0, 10), false);
        verify(requestMock).setAttribute("servicesPage", servicesPage);
        verify(requestMock).setAttribute("supportArchived", true);
    }

    @Test
    public void executeShouldGetServicesPageWhenPageIsSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn("10");
        when(requestMock.getParameter("archived")).thenReturn("true");

        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).getPage( new Pageable(9, 10), true);
        verify(requestMock).setAttribute("servicesPage", servicesPage);
        verify(requestMock).setAttribute("supportArchived", true);
    }
}
