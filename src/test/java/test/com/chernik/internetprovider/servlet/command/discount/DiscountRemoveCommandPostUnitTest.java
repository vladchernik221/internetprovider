package test.com.chernik.internetprovider.servlet.command.discount;

import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.impl.discount.DiscountRemoveCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import static org.mockito.Mockito.*;

public class DiscountRemoveCommandPostUnitTest extends CommandUnitTest {
    private DiscountRemoveCommandPost command;
    private DiscountService discountService;

    @BeforeClass
    public void init() {
        super.init();
        command = new DiscountRemoveCommandPost();
        discountService = mock(DiscountService.class);
        command.setDiscountService(discountService);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(discountService);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/discount/5/remove");
    }

    @Test
    public void executeShouldRemoveDiscountViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(discountService).remove(5L);
    }
}
