package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.ServiceMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.io.PrintWriter;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ServiceCreateCommandPostUnitTest extends CommandUnitTest {
    private ServiceCreateCommandPost command;
    private ServiceService serviceServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceCreateCommandPost();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);
        command.setServiceMapper(getMapper(ServiceMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getParameter("name")).thenReturn("New service");
        when(requestMock.getParameter("description")).thenReturn("New service description");
        when(requestMock.getParameter("price")).thenReturn("30.5");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(serviceServiceMock.create(any(Service.class))).thenReturn(15L);
    }

    @Test
    public void executeShouldSaveServiceViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(serviceServiceMock).create(captor.capture());
        assertEquals(captor.getValue(), createTestService());
    }

    @Test
    public void executeShouldReturnGeneratedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(printWriterMock).write("15");
    }

    private Service createTestService() {
        Service service = new Service();
        service.setName("New service");
        service.setDescription("New service description");
        service.setPrice(new BigDecimal("30.5"));
        return service;
    }
}
