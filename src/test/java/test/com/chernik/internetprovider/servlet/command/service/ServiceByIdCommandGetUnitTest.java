package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceByIdCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.util.CommandUnitTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServiceByIdCommandGetUnitTest extends CommandUnitTest {
    private ServiceByIdCommandGet command;
    private ServiceService serviceServiceMock;

    private Service service;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceByIdCommandGet();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);

        service = mock(Service.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/service/5");
        when(serviceServiceMock.getById(anyLong())).thenReturn(service);
    }

    @Test
    public void executeShouldGetServiceWithSpecifiedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).getById(5L);
        verify(requestMock).setAttribute("service", service);
    }
}
