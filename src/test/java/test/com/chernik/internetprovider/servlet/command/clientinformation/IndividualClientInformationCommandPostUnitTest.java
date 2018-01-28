package test.com.chernik.internetprovider.servlet.command.clientinformation;

import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.service.IndividualClientInformationService;
import com.chernik.internetprovider.servlet.command.impl.clientinformation.IndividualClientInformationCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.util.CommandUnitTest;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class IndividualClientInformationCommandPostUnitTest extends CommandUnitTest {
    private IndividualClientInformationCommandPost command;
    private IndividualClientInformationService clientInformationServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new IndividualClientInformationCommandPost();
        clientInformationServiceMock = mock(IndividualClientInformationService.class);
        command.setIndividualClientInformationService(clientInformationServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(clientInformationServiceMock);
        super.resetMocks();
        when(requestMock.getParameter("identifier")).thenReturn("test passport id");
        when(clientInformationServiceMock.getByPassportData(anyString())).thenReturn(createTestClientInformation());
    }

    @Test
    public void executeShouldReturnIndividualClientInformationWhenExists() throws Exception {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriter);

        command.execute(requestMock, responseMock);
        verify(clientInformationServiceMock).getByPassportData("test passport id");
        verify(printWriter).write(new ObjectMapper().writeValueAsString(createTestClientInformation()));
    }

    @Test
    public void executeShouldReturnNullWhenDoesNotExist() throws Exception {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriter);
        when(clientInformationServiceMock.getByPassportData(anyString())).thenReturn(null);

        command.execute(requestMock, responseMock);
        verify(clientInformationServiceMock).getByPassportData("test passport id");
        verify(printWriter).write("null");
    }

    private IndividualClientInformation createTestClientInformation() {
        IndividualClientInformation clientInformation = new IndividualClientInformation(10L);
        clientInformation.setFirstName("Ivan");
        clientInformation.setSecondName("Nikolaevich");
        clientInformation.setLastName("Petrov");
        clientInformation.setPassportUniqueIdentification("test passport id");
        clientInformation.setAddress("Test address 1");
        clientInformation.setPhoneNumber("(44)777-66-55");
        return clientInformation;
    }
}
