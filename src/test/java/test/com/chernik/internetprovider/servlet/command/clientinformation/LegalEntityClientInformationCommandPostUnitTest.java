package test.com.chernik.internetprovider.servlet.command.clientinformation;

import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;
import com.chernik.internetprovider.servlet.command.impl.clientinformation.LegalEntityClientInformationCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class LegalEntityClientInformationCommandPostUnitTest extends CommandUnitTest {
    private LegalEntityClientInformationCommandPost command;
    private LegalEntityClientInformationService clientInformationServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new LegalEntityClientInformationCommandPost();
        clientInformationServiceMock = mock(LegalEntityClientInformationService.class);
        command.setLegalEntityClientInformationService(clientInformationServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(clientInformationServiceMock);
        super.resetMocks();
        when(requestMock.getParameter("identifier")).thenReturn("1234567890");
        when(clientInformationServiceMock.getByPayerAccountNumber(anyString())).thenReturn(createTestClientInformation());
    }

    @Test
    public void executeShouldReturnLegalEntityClientInformationWhenExists() throws Exception {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriter);

        command.execute(requestMock, responseMock);
        verify(clientInformationServiceMock).getByPayerAccountNumber("1234567890");
        verify(printWriter).write(new ObjectMapper().writeValueAsString(createTestClientInformation()));
    }

    @Test
    public void executeShouldReturnNullWhenDoesNotExist() throws Exception {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriter);
        when(clientInformationServiceMock.getByPayerAccountNumber(anyString())).thenReturn(null);

        command.execute(requestMock, responseMock);
        verify(clientInformationServiceMock).getByPayerAccountNumber("1234567890");
        verify(printWriter).write("null");
    }

    private LegalEntityClientInformation createTestClientInformation() {
        LegalEntityClientInformation clientInformation = new LegalEntityClientInformation(10L);
        clientInformation.setName("Рога и копыта =)");
        clientInformation.setPayerAccountNumber("1234567890");
        clientInformation.setCheckingAccount("0987654321");
        clientInformation.setAddress("г. Минск, ул. Ленина, д. 5, кв. 11");
        clientInformation.setPhoneNumber("(44)777-66-55");
        return clientInformation;
    }
}
