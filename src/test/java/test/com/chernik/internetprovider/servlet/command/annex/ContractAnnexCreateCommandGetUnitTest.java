package test.com.chernik.internetprovider.servlet.command.annex;

import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.annex.ContractAnnexCreateCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ContractAnnexCreateCommandGetUnitTest extends CommandUnitTest {
    private ContractAnnexCreateCommandGet command;
    private TariffPlanService tariffPlanServiceMock;
    private ContractService contractServiceMock;

    private List<TariffPlan> tariffPlanList;

    @BeforeClass
    public void init() {
        super.init();
        command = new ContractAnnexCreateCommandGet();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        contractServiceMock = mock(ContractService.class);
        command.setTariffPlanService(tariffPlanServiceMock);
        command.setContractService(contractServiceMock);

        tariffPlanList = Arrays.asList(mock(TariffPlan.class), mock(TariffPlan.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock, contractServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/5/annex/new");
        when(contractServiceMock.existById(anyLong())).thenReturn(true);
        when(tariffPlanServiceMock.getAllNotArchived()).thenReturn(tariffPlanList);
    }

    @Test
    public void executeShouldReturnPageWithTariffPlans() throws Exception {
        command.execute(requestMock, responseMock);
        verify(contractServiceMock).existById(5L);
        verify(tariffPlanServiceMock).getAllNotArchived();
        verify(requestMock).setAttribute("tariffPlans", tariffPlanList);
        verify(requestMock).setAttribute("contractId", "5");
    }
}
