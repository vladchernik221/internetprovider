package test.com.chernik.internetprovider.servlet.command.annex;

import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.servlet.command.impl.annex.ContractAnnexCancelCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ContractAnnexCancelCommandPostUnitTest extends CommandUnitTest {
    private ContractAnnexCancelCommandPost command;
    private ContractAnnexService contractAnnexService;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractAnnexCancelCommandPost();
        contractAnnexService = mock(ContractAnnexService.class);
        command.setContractAnnexService(contractAnnexService);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractAnnexService);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/annex/5/cancel");
    }

    @Test
    public void executeShouldChangeContractAnnexStatusViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractAnnexService).cancel(5L);
    }
}
