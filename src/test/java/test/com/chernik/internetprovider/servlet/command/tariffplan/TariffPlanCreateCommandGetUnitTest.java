package test.com.chernik.internetprovider.servlet.command.tariffplan;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.impl.tariffplan.TariffPlanCreateCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class TariffPlanCreateCommandGetUnitTest extends CommandUnitTest {
    private TariffPlanCreateCommandGet command;
    private DiscountService discountServiceMock;

    private List<Discount> discountList;

    @BeforeClass
    public void init() {
        super.init();
        command = new TariffPlanCreateCommandGet();
        discountServiceMock = mock(DiscountService.class);
        command.setDiscountService(discountServiceMock);

        discountList = Arrays.asList(mock(Discount.class), mock(Discount.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(discountServiceMock);
        super.resetMocks();
        when(discountServiceMock.getAll()).thenReturn(discountList);
    }

    @Test
    public void executeShouldReturnPageWithDiscounts() throws Exception {
        command.execute(requestMock, responseMock);
        verify(discountServiceMock).getAll();
        verify(requestMock).setAttribute("discounts", discountList);
    }
}
