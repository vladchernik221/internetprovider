package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceEditCommandPost;
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

public class ServiceEditCommandPostUnitTest extends CommandUnitTest {
    private ServiceEditCommandPost command;
    private ServiceService serviceServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceEditCommandPost();
        serviceServiceMock = mock(ServiceService.class);
        command.setServiceService(serviceServiceMock);
        command.setServiceMapper(getMapper(ServiceMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(serviceServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/service/5");
        when(requestMock.getParameter("name")).thenReturn("Новый сервис");
        when(requestMock.getParameter("description")).thenReturn("Описание нового сервиса");
        when(requestMock.getParameter("price")).thenReturn("30.5");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
    }

    @Test
    public void executeShouldSaveServiceViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(serviceServiceMock).update(captor.capture());
        assertEquals(captor.getValue(), createTestService());
    }

    private Service createTestService() {
        Service service = new Service(5L);
        service.setName("Новый сервис");
        service.setDescription("Описание нового сервиса");
        service.setPrice(new BigDecimal("30.5"));
        return service;
    }
}
