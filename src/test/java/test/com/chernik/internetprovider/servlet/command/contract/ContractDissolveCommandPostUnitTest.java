package test.com.chernik.internetprovider.servlet.command.contract;

import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.impl.contract.ContractDissolveCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ContractDissolveCommandPostUnitTest extends CommandUnitTest {
    private ContractDissolveCommandPost command;
    private ContractService contractService;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractDissolveCommandPost();
        contractService = mock(ContractService.class);
        command.setContractService(contractService);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractService);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/5/dissolve");
    }

    @Test
    public void executeShouldChangeContractStatusViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractService).dissolve(5L);
    }
}
