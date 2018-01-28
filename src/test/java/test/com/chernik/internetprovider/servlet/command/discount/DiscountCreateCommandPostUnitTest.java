package test.com.chernik.internetprovider.servlet.command.discount;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.servlet.command.impl.discount.DiscountCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.DiscountMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import java.io.PrintWriter;
import java.sql.Date;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class DiscountCreateCommandPostUnitTest extends CommandUnitTest {
    private DiscountCreateCommandPost command;
    private DiscountService discountServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new DiscountCreateCommandPost();
        discountServiceMock = mock(DiscountService.class);
        command.setDiscountService(discountServiceMock);
        command.setDiscountMapper(getMapper(DiscountMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(discountServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getParameter("name")).thenReturn("Новая акция");
        when(requestMock.getParameter("description")).thenReturn("Описание новой акции");
        when(requestMock.getParameter("amount")).thenReturn("30");
        when(requestMock.getParameter("startDate")).thenReturn("2018-02-01");
        when(requestMock.getParameter("endDate")).thenReturn("2018-03-01");
        when(requestMock.getParameter("onlyForNewClient")).thenReturn("true");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(discountServiceMock.create(any(Discount.class))).thenReturn(15L);
    }

    @Test
    public void executeShouldSaveDiscountViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<Discount> captor = ArgumentCaptor.forClass(Discount.class);
        verify(discountServiceMock).create(captor.capture());
        assertEquals(captor.getValue(), createTestDiscount());
    }

    @Test
    public void executeShouldReturnGeneratedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(printWriterMock).write("15");
    }

    private Discount createTestDiscount() {
        Discount discount = new Discount();
        discount.setName("Новая акция");
        discount.setDescription("Описание новой акции");
        discount.setAmount(30);
        discount.setStartDate(Date.valueOf("2018-02-01"));
        discount.setEndDate(Date.valueOf("2018-03-01"));
        discount.setOnlyForNewClient(true);
        return discount;
    }
}
