package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanByIdCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TariffPlanByIdCommandGetUnitTest extends CommandUnitTest {
    private TariffPlanByIdCommandGet command;
    private TariffPlanService tariffPlanServiceMock;

    private TariffPlan tariffPlan;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanByIdCommandGet();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        command.setTariffPlanService(tariffPlanServiceMock);

        tariffPlan = mock(TariffPlan.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/tariff-plan/5");
        when(tariffPlanServiceMock.getById(anyLong())).thenReturn(tariffPlan);
    }

    @Test
    public void executeShouldGetTariffPlanWithSpecifiedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(tariffPlanServiceMock).getById(5L);
        verify(requestMock).setAttribute("tariffPlan", tariffPlan);
    }
}
