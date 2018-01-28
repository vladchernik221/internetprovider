package test.com.chernik.internetprovider.servlet.command.annex;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.servlet.command.impl.annex.ContractAnnexListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ContractAnnexListCommandGetUnitTest extends CommandUnitTest {
    private ContractAnnexListCommandGet command;
    private ContractAnnexService contractAnnexServiceMock;

    private Page<ContractAnnex> contractAnnexesPage;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractAnnexListCommandGet();
        contractAnnexServiceMock = mock(ContractAnnexService.class);
        command.setContractAnnexService(contractAnnexServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        contractAnnexesPage = new Page<>(Arrays.asList(mock(ContractAnnex.class), mock(ContractAnnex.class)), 7);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractAnnexServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/5/annex");
        when(requestMock.getParameter("page")).thenReturn("10");
        when(contractAnnexServiceMock.getPage(anyLong(), any(Pageable.class))).thenReturn(contractAnnexesPage);
    }

    @Test
    public void executeShouldGetFirstContractAnnexPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);

        command.execute(requestMock, responseMock);
        verify(contractAnnexServiceMock).getPage(5L, new Pageable(0, 10));
        verify(requestMock).setAttribute("contractAnnexesPage", contractAnnexesPage);
        verify(requestMock).setAttribute("contractId", 5L);
    }

    @Test
    public void executeShouldGetContractAnnexPageWhenPageIsSpecified() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractAnnexServiceMock).getPage(5L, new Pageable(9, 10));
        verify(requestMock).setAttribute("contractAnnexesPage", contractAnnexesPage);
        verify(requestMock).setAttribute("contractId", 5L);
    }
}
