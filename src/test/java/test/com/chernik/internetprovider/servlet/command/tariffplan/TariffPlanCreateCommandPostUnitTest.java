package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.TariffPlanMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class TariffPlanCreateCommandPostUnitTest extends CommandUnitTest {
    private TariffPlanCreateCommandPost command;
    private TariffPlanService tariffPlanServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanCreateCommandPost();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        command.setTariffPlanService(tariffPlanServiceMock);
        command.setTariffPlanMapper(getMapper(TariffPlanMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getParameter("name")).thenReturn("New tariff plan");
        when(requestMock.getParameter("description")).thenReturn("New tariff plan description");
        when(requestMock.getParameter("downSpeed")).thenReturn("100");
        when(requestMock.getParameter("upSpeed")).thenReturn("200");
        when(requestMock.getParameter("includedTraffic")).thenReturn("50");
        when(requestMock.getParameter("priceOverTraffic")).thenReturn("5.7");
        when(requestMock.getParameter("monthlyFee")).thenReturn("10.99");
        when(requestMock.getParameter("discounts")).thenReturn("1;2;6");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(tariffPlanServiceMock.create(any(TariffPlan.class))).thenReturn(5L);
    }

    @Test
    public void executeShouldSaveTariffPlanViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<TariffPlan> captor = ArgumentCaptor.forClass(TariffPlan.class);
        verify(tariffPlanServiceMock).create(captor.capture());
        assertEquals(captor.getValue(), createTestTariffPlan());
    }

    @Test
    public void executeShouldReturnGeneratedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(printWriterMock).write("5");
    }

    private TariffPlan createTestTariffPlan() {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setName("New tariff plan");
        tariffPlan.setDescription("New tariff plan description");
        tariffPlan.setDownSpeed(100);
        tariffPlan.setUpSpeed(200);
        tariffPlan.setIncludedTraffic(50);
        tariffPlan.setPriceOverTraffic(new BigDecimal("5.7"));
        tariffPlan.setMonthlyFee(new BigDecimal("10.99"));
        tariffPlan.setDiscounts(Arrays.asList(new Discount(1L), new Discount(2L), new Discount(6L)));
        return tariffPlan;
    }
}
