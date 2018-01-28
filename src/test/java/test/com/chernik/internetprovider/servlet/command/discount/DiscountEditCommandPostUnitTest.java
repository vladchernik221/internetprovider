package test.com.chernik.internetprovider.servlet.command.discount;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.impl.discount.DiscountEditCommandPost;
import com.chernik.internetprovider.servlet.mapper.DiscountMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.util.CommandUnitTest;

import java.io.PrintWriter;
import java.sql.Date;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class DiscountEditCommandPostUnitTest extends CommandUnitTest {
    private DiscountEditCommandPost command;
    private DiscountService discountServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new DiscountEditCommandPost();
        discountServiceMock = mock(DiscountService.class);
        command.setDiscountService(discountServiceMock);
        command.setDiscountMapper(getMapper(DiscountMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(discountServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/discount/5");
        when(requestMock.getParameter("name")).thenReturn("New action");
        when(requestMock.getParameter("description")).thenReturn("New action description");
        when(requestMock.getParameter("amount")).thenReturn("30");
        when(requestMock.getParameter("startDate")).thenReturn("2018-02-01");
        when(requestMock.getParameter("endDate")).thenReturn("2018-03-01");
        when(requestMock.getParameter("onlyForNewClient")).thenReturn("true");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
    }

    @Test
    public void executeShouldSaveDiscountViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<Discount> captor = ArgumentCaptor.forClass(Discount.class);
        verify(discountServiceMock).update(captor.capture());
        assertEquals(captor.getValue(), createTestDiscount());
    }

    private Discount createTestDiscount() {
        Discount discount = new Discount(5L);
        discount.setName("New action");
        discount.setDescription("New action description");
        discount.setAmount(30);
        discount.setStartDate(Date.valueOf("2018-02-01"));
        discount.setEndDate(Date.valueOf("2018-03-01"));
        discount.setOnlyForNewClient(true);
        return discount;
    }
}
