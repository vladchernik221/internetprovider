package test.com.chernik.internetprovider.servlet.command.annex;

import com.chernik.internetprovider.persistence.entity.*;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.servlet.command.impl.annex.ContractAnnexCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.ContractAnnexMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ContractAnnexCreateCommandPostUnitTest extends CommandUnitTest {
    private ContractAnnexCreateCommandPost command;
    private ContractAnnexService contractAnnexServiceMock;
    private User testUser;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        testUser = createTestUser();
        command = new ContractAnnexCreateCommandPost();
        contractAnnexServiceMock = mock(ContractAnnexService.class);
        command.setContractAnnexService(contractAnnexServiceMock);
        command.setContractAnnexMapper(getMapper(ContractAnnexMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractAnnexServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/5/annex/new");
        when(requestMock.getParameter("address")).thenReturn("Test address 1");
        when(requestMock.getParameter("tariffPlanId")).thenReturn("10");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(contractAnnexServiceMock.create(any(ContractAnnex.class), any(User.class))).thenReturn(15L);
        when(sessionMock.getAttribute("user")).thenReturn(testUser);
    }

    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUserRole(UserRole.ADMIN);
        return user;
    }

    @Test
    public void executeShouldSaveContractAnnexViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<ContractAnnex> captor = ArgumentCaptor.forClass(ContractAnnex.class);
        verify(contractAnnexServiceMock).create(captor.capture(), eq(testUser));
        assertEquals(captor.getValue(), createTestContractAnnex());
    }

    @Test
    public void executeShouldReturnGeneratedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(printWriterMock).write("15");
    }

    private ContractAnnex createTestContractAnnex() {
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContract(new Contract(5L));
        contractAnnex.setAddress("Test address 1");
        contractAnnex.setTariffPlan(new TariffPlan(10L));
        return contractAnnex;
    }
}
