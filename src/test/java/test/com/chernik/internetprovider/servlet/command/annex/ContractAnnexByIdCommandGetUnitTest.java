package test.com.chernik.internetprovider.servlet.command.annex;

import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.servlet.command.impl.annex.ContractAnnexByIdCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ContractAnnexByIdCommandGetUnitTest extends CommandUnitTest {
    private ContractAnnexByIdCommandGet command;
    private ContractAnnexService contractAnnexServiceMock;
    private User testUser = new User();

    private ContractAnnex contractAnnex;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractAnnexByIdCommandGet();
        contractAnnexServiceMock = mock(ContractAnnexService.class);
        command.setContractAnnexService(contractAnnexServiceMock);

        contractAnnex = mock(ContractAnnex.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractAnnexServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/annex/5");
        when(contractAnnexServiceMock.getById(anyLong(), any(User.class))).thenReturn(contractAnnex);
        when(sessionMock.getAttribute("user")).thenReturn(testUser);
    }

    @Test
    public void executeShouldGetContractAnnexWithSpecifiedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractAnnexServiceMock).getById(5L, testUser);
        verify(requestMock).setAttribute("annex", contractAnnex);
    }
}
