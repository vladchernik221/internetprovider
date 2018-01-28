package test.com.chernik.internetprovider.servlet.command.annex;

import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.servlet.command.impl.annex.ContractAnnexCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.ContractAnnexMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ContractAnnexCreateCommandPostUnitTest extends CommandUnitTest {
    private ContractAnnexCreateCommandPost command;
    private ContractAnnexService contractAnnexServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
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
        when(requestMock.getParameter("address")).thenReturn("г. Минск, ул. Ленина, д. 5, кв. 11");
        when(requestMock.getParameter("tariffPlanId")).thenReturn("10");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(contractAnnexServiceMock.create(any(ContractAnnex.class))).thenReturn(15L);
    }

    @Test
    public void executeShouldSaveContractAnnexViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<ContractAnnex> captor = ArgumentCaptor.forClass(ContractAnnex.class);
        verify(contractAnnexServiceMock).create(captor.capture());
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
        contractAnnex.setAddress("г. Минск, ул. Ленина, д. 5, кв. 11");
        contractAnnex.setTariffPlan(new TariffPlan(10L));
        return contractAnnex;
    }
}
