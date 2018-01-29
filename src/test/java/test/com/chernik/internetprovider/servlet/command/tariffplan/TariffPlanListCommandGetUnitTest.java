package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class TariffPlanListCommandGetUnitTest extends CommandUnitTest {
    private TariffPlanListCommandGet command;
    private TariffPlanService tariffPlanServiceMock;

    private Page<TariffPlan> tariffPlanPage;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanListCommandGet();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        command.setTariffPlanService(tariffPlanServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        tariffPlanPage = new Page<>(Arrays.asList(mock(TariffPlan.class), mock(TariffPlan.class)), 7);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock);
        super.resetMocks();
        when(tariffPlanServiceMock.getPage(any(Pageable.class), anyBoolean())).thenReturn(tariffPlanPage);
    }

    @Test
    public void executeShouldGetFirstTariffPlanPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);
        when(requestMock.getParameter("archived")).thenReturn("false");

        command.execute(requestMock, responseMock);
        verify(tariffPlanServiceMock).getPage(new Pageable(0, 10), false);
        verify(requestMock).setAttribute("tariffPlansPage", tariffPlanPage);
    }

    @Test
    public void executeShouldGetTariffPlanPageWhenPageIsSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn("10");
        when(requestMock.getParameter("archived")).thenReturn("true");

        command.execute(requestMock, responseMock);
        verify(tariffPlanServiceMock).getPage( new Pageable(9, 10), true);
        verify(requestMock).setAttribute("tariffPlansPage", tariffPlanPage);
    }
}
