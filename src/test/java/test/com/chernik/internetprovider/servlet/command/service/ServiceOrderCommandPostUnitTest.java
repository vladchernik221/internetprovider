package test.com.chernik.internetprovider.servlet.command.service;

import com.chernik.internetprovider.service.ContractAnnexHasServiceService;
import com.chernik.internetprovider.servlet.command.impl.service.ServiceOrderCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ServiceOrderCommandPostUnitTest extends CommandUnitTest {
    private ServiceOrderCommandPost command;
    private ContractAnnexHasServiceService contractAnnexHasServiceServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new ServiceOrderCommandPost();
        contractAnnexHasServiceServiceMock = mock(ContractAnnexHasServiceService.class);
        command.setContractAnnexHasServiceService(contractAnnexHasServiceServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractAnnexHasServiceServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/annex/5/service");
        when(requestMock.getParameter("serviceId")).thenReturn("10");
    }

    @Test
    public void executeShouldAddServiceToContractAnnex() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractAnnexHasServiceServiceMock).create(5L, 10L);
    }
}
