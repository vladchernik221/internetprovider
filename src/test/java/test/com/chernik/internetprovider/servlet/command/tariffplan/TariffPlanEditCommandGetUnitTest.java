package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanEditCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TariffPlanEditCommandGetUnitTest extends CommandUnitTest {
    private TariffPlanEditCommandGet command;
    private TariffPlanService tariffPlanServiceMock;
    private DiscountService discountServiceMock;

    private TariffPlan tariffPlan;
    private List<Discount> discountList;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanEditCommandGet();
        tariffPlanServiceMock = mock(TariffPlanService.class);
        discountServiceMock = mock(DiscountService.class);
        command.setTariffPlanService(tariffPlanServiceMock);
        command.setDiscountService(discountServiceMock);

        tariffPlan = mock(TariffPlan.class);
        discountList = Arrays.asList(mock(Discount.class), mock(Discount.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(tariffPlanServiceMock, discountServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/tariff-plan/5/edit");
        when(tariffPlanServiceMock.getById(anyLong())).thenReturn(tariffPlan);
        when(discountServiceMock.getAll()).thenReturn(discountList);
    }

    @Test
    public void executeShouldReturnPageWithTariffPlanAndDiscounts() throws Exception {
        command.execute(requestMock, responseMock);
        verify(tariffPlanServiceMock).getById(5L);
        verify(discountServiceMock).getAll();
        verify(requestMock).setAttribute("tariffPlan", tariffPlan);
        verify(requestMock).setAttribute("discounts", discountList);
    }
}
