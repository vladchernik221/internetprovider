package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanEditCommandPost;
import com.chernik.internetprovider.servlet.mapper.TariffPlanMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class TariffPlanEditCommandPostUnitTest extends CommandUnitTest {
    private TariffPlanEditCommandPost command;
    private TariffPlanService tariffPlanServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanEditCommandPost();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        command.setTariffPlanService(tariffPlanServiceMock);
        command.setTariffPlanMapper(getMapper(TariffPlanMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/tariff-plan/5");
        when(requestMock.getParameter("name")).thenReturn("New tariff plan");
        when(requestMock.getParameter("description")).thenReturn("New tariff plan description");
        when(requestMock.getParameter("downSpeed")).thenReturn("100");
        when(requestMock.getParameter("upSpeed")).thenReturn("200");
        when(requestMock.getParameter("includedTraffic")).thenReturn("50");
        when(requestMock.getParameter("priceOverTraffic")).thenReturn("5.7");
        when(requestMock.getParameter("monthlyFee")).thenReturn("10.99");
        when(requestMock.getParameter("discounts")).thenReturn("");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
    }

    @Test
    public void executeShouldSaveTariffPlanViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<TariffPlan> captor = ArgumentCaptor.forClass(TariffPlan.class);
        verify(tariffPlanServiceMock).update(captor.capture());
        assertEquals(captor.getValue(), createTestTariffPlan());
    }

    private TariffPlan createTestTariffPlan() {
        TariffPlan tariffPlan = new TariffPlan(5L);
        tariffPlan.setName("New tariff plan");
        tariffPlan.setDescription("New tariff plan description");
        tariffPlan.setDownSpeed(100);
        tariffPlan.setUpSpeed(200);
        tariffPlan.setIncludedTraffic(50);
        tariffPlan.setPriceOverTraffic(new BigDecimal("5.7"));
        tariffPlan.setMonthlyFee(new BigDecimal("10.99"));
        tariffPlan.setDiscounts(Collections.emptyList());
        return tariffPlan;
    }
}
