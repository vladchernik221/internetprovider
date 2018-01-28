package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceArchiveCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ServiceArchiveCommandPostUnitTest extends CommandUnitTest {
    private ServiceArchiveCommandPost command;
    private ServiceService serviceServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceArchiveCommandPost();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/service/5/archive");
    }

    @Test
    public void executeShouldUpdateServiceStatusViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(serviceServiceMock).archive(5L);
    }
}
