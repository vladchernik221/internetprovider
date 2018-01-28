package test.com.chernik.internetprovider.servlet.command.contract;

import com.chernik.internetprovider.persistence.entity.ClientType;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.impl.contract.ContractCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.ContractMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ContractCreateCommandPostUnitTest extends CommandUnitTest {
    private ContractCreateCommandPost command;
    private ContractService contractServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractCreateCommandPost();
        contractServiceMock = mock(ContractService.class);
        command.setContractService(contractServiceMock);
        command.setContractMapper(getMapper(ContractMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getParameter("individual.firstName")).thenReturn("Ivan");
        when(requestMock.getParameter("individual.secondName")).thenReturn("Nikolaevich");
        when(requestMock.getParameter("individual.lastName")).thenReturn("Petrov");
        when(requestMock.getParameter("individual.address")).thenReturn("Test address 1");
        when(requestMock.getParameter("individual.passportUniqueIdentification")).thenReturn("test passport id");
        when(requestMock.getParameter("individual.phoneNumber")).thenReturn("(44)777-66-55");

        when(requestMock.getParameter("legal.name")).thenReturn("Roga i kopi'ta =)");
        when(requestMock.getParameter("legal.payerAccountNumber")).thenReturn("1234567890");
        when(requestMock.getParameter("legal.checkingAccount")).thenReturn("0987654321");
        when(requestMock.getParameter("legal.address")).thenReturn("Test address 1");
        when(requestMock.getParameter("legal.phoneNumber")).thenReturn("(44)444-33-22");

        when(requestMock.getParameter("password")).thenReturn("test password");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(contractServiceMock.create(any(Contract.class), anyString())).thenReturn(15L);
    }

    @Test
    public void executeShouldSaveContractForIndividualViaService() throws Exception {
        when(requestMock.getParameter("clientType")).thenReturn("individual");

        command.execute(requestMock, responseMock);

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        verify(contractServiceMock).create(captor.capture(), eq("test password"));
        assertEquals(captor.getValue(), createTestContractForIndividual());
    }

    @Test
    public void executeShouldSaveContractForLegalEntityViaService() throws Exception {
        when(requestMock.getParameter("clientType")).thenReturn("legal");

        command.execute(requestMock, responseMock);

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        verify(contractServiceMock).create(captor.capture(), eq("test password"));
        assertEquals(captor.getValue(), createTestContractForLegalEntity());
    }

    @Test
    public void executeShouldReturnGeneratedId() throws Exception {
        when(requestMock.getParameter("clientType")).thenReturn("individual");
        command.execute(requestMock, responseMock);
        verify(printWriterMock).write("15");
    }

    private Contract createTestContractForIndividual() {
        Contract contract = new Contract();
        contract.setClientType(ClientType.INDIVIDUAL);

        IndividualClientInformation clientInformation = new IndividualClientInformation();
        clientInformation.setFirstName("Ivan");
        clientInformation.setSecondName("Nikolaevich");
        clientInformation.setLastName("Petrov");
        clientInformation.setPassportUniqueIdentification("test passport id");
        clientInformation.setAddress("Test address 1");
        clientInformation.setPhoneNumber("(44)777-66-55");
        contract.setIndividualClientInformation(clientInformation);
        return contract;
    }

    private Contract createTestContractForLegalEntity() {
        Contract contract = new Contract();
        contract.setClientType(ClientType.LEGAL);

        LegalEntityClientInformation clientInformation = new LegalEntityClientInformation();
        clientInformation.setName("Roga i kopi'ta =)");
        clientInformation.setPayerAccountNumber("1234567890");
        clientInformation.setCheckingAccount("0987654321");
        clientInformation.setAddress("Test address 1");
        clientInformation.setPhoneNumber("(44)444-33-22");
        contract.setLegalEntityClientInformation(clientInformation);
        return contract;
    }
}
