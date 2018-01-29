package test.com.chernik.internetprovider.servlet.command.contract;

import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.servlet.command.impl.contract.ContractListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ContractListCommandGetUnitTest extends CommandUnitTest {
    private ContractListCommandGet command;
    private ContractService contractServiceMock;

    private Contract contract;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractListCommandGet();
        contractServiceMock = mock(ContractService.class);
        command.setContractService(contractServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        contract = mock(Contract.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(contractServiceMock);
        super.resetMocks();
        when(requestMock.getParameter("number")).thenReturn("000005");
        when(contractServiceMock.getById(anyLong())).thenReturn(contract);
    }

    @Test
    public void executeShouldReturnContractWhenExist() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractServiceMock).getById(5L);
        verify(requestMock).setAttribute("contract", contract);
    }
}
