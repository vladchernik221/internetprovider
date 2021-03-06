package test.com.chernik.internetprovider.servlet.command.contract;

import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.impl.contract.ContractByIdCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ContractByIdCommandGetUnitTest extends CommandUnitTest {
    private ContractByIdCommandGet command;
    private ContractService contractServiceMock;
    private User testUser = new User();

    private Contract contract;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractByIdCommandGet();
        contractServiceMock = mock(ContractService.class);
        command.setContractService(contractServiceMock);

        contract = mock(Contract.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/5");
        when(contractServiceMock.getByIdOrThrow(anyLong(), any(User.class))).thenReturn(contract);
        when(sessionMock.getAttribute("user")).thenReturn(testUser);
    }

    @Test
    public void executeShouldGetContractWithSpecifiedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractServiceMock).getByIdOrThrow(5L, testUser);
        verify(requestMock).setAttribute("contract", contract);
    }
}
