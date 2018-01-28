package test.com.chernik.internetprovider.servlet.command.discount;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.impl.discount.DiscountListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.util.CommandUnitTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class DiscountListCommandGetUnitTest extends CommandUnitTest {
    private DiscountListCommandGet command;
    private DiscountService discountServiceMock;

    private Page<Discount> discountsPage;

    @BeforeClass
    public void init() {
        super.init();
        command = new DiscountListCommandGet();
        discountServiceMock = mock(DiscountService.class);
        command.setDiscountService(discountServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        discountsPage = new Page<>(Arrays.asList(mock(Discount.class), mock(Discount.class)), 7);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(discountServiceMock);
        super.resetMocks();
        when(requestMock.getParameter("page")).thenReturn("10");
        when(discountServiceMock.getPage(any(Pageable.class))).thenReturn(discountsPage);
    }

    @Test
    public void executeShouldGetFirstDiscountsPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);

        command.execute(requestMock, responseMock);
        verify(discountServiceMock).getPage(new Pageable(0, 10));
        verify(requestMock).setAttribute("discountsPage", discountsPage);
    }

    @Test
    public void executeShouldGetDiscountsPageWhenPageIsSpecified() throws Exception {
        command.execute(requestMock, responseMock);
        verify(discountServiceMock).getPage( new Pageable(9, 10));
        verify(requestMock).setAttribute("discountsPage", discountsPage);
    }
}
