package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanArchiveCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class TariffPlanArchiveCommandPostUnitTest extends CommandUnitTest {
    private TariffPlanArchiveCommandPost command;
    private TariffPlanService tariffPlanServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanArchiveCommandPost();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        command.setTariffPlanService(tariffPlanServiceMock);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/tariff-plan/5/archive");
    }

    @Test
    public void executeShouldUpdateTariffPlanStatusViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(tariffPlanServiceMock).archive(5L);
    }
}
