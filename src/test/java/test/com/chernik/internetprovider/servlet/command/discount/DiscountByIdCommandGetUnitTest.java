package test.com.chernik.internetprovider.servlet.command.discount;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.impl.discount.DiscountByIdCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.util.CommandUnitTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DiscountByIdCommandGetUnitTest extends CommandUnitTest {
    private DiscountByIdCommandGet command;
    private DiscountService discountServiceMock;

    private Discount discount;

    @BeforeClass
    public void init() {
        super.init();
        command = new DiscountByIdCommandGet();
        discountServiceMock = mock(DiscountService.class);
        command.setDiscountService(discountServiceMock);

        discount = mock(Discount.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(discountServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/discount/5");
        when(discountServiceMock.getById(anyLong())).thenReturn(discount);
    }

    @Test
    public void executeShouldGetDiscountWithSpecifiedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(discountServiceMock).getById(5L);
        verify(requestMock).setAttribute("discount", discount);
    }
}
