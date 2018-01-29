package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceEditCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServiceEditCommandGetUnitTest extends CommandUnitTest {
    private ServiceEditCommandGet command;
    private ServiceService serviceServiceMock;

    private Service service;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceEditCommandGet();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);

        service = mock(Service.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/service/5/edit");
        when(serviceServiceMock.getById(anyLong())).thenReturn(service);
    }

    @Test
    public void executeShouldReturnPageWithService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).getById(5L);
        verify(requestMock).setAttribute("service", service);
    }
}
