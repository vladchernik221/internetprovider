package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.repository.DiscountRepository;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.service.impl.DiscountServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

public class DiscountServiceUnitTest {
    private DiscountServiceImpl discountService;

    private DiscountRepository discountRepositoryMock = mock(DiscountRepository.class);
    private TariffPlanService tariffPlanServiceMock = mock(TariffPlanService.class);

    @BeforeClass
    public void init() {
        discountService = new DiscountServiceImpl();
        discountService.setDiscountRepository(discountRepositoryMock);
        discountService.setTariffPlanService(tariffPlanServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(discountRepositoryMock, tariffPlanServiceMock);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void createShouldThrowExceptionWhenDiscountWithNameAlreadyExists() throws Exception {
        when(discountRepositoryMock.existWithName(anyString())).thenReturn(true);
        discountService.create(createTestDiscount());
    }

    @Test
    public void createShouldReturnGeneratedId() throws Exception {
        when(discountRepositoryMock.existWithName(anyString())).thenReturn(false);
        when(discountRepositoryMock.create(any(Discount.class))).thenReturn(5L);

        Discount discount = createTestDiscount();
        Long generatedId = discountService.create(discount);

        assertEquals(generatedId.longValue(), 5L);
        verify(discountRepositoryMock).existWithName("test name");
        verify(discountRepositoryMock).create(discount);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void updateShouldThrowExceptionWhenDiscountDoeesNotExist() throws Exception {
        when(discountRepositoryMock.existWithId(anyLong())).thenReturn(false);
        discountService.update(new Discount(5L));
    }

    @Test
    public void updateShouldSaveUpdatedDiscount() throws Exception {
        when(discountRepositoryMock.existWithId(anyLong())).thenReturn(true);

        Discount discount = new Discount(5L);
        discountService.update(discount);
        verify(discountRepositoryMock).existWithId(5L);
        verify(discountRepositoryMock).update(discount);
    }

    @Test
    public void getPageShouldReturnPageOfDiscounts() throws Exception {
        Page<Discount> page = new Page<>(Arrays.asList(mock(Discount.class), mock(Discount.class)), 5);
        when(discountRepositoryMock.getPage(any(Pageable.class))).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<Discount> actualPage = discountService.getPage(pageable);
        assertSame(actualPage, page);
        verify(discountRepositoryMock).getPage(pageable);
    }

    @Test
    public void getAllShouldReturnListOfAllDiscounts() throws Exception {
        List<Discount> discountList = Arrays.asList(mock(Discount.class), mock(Discount.class));
        when(discountRepositoryMock.getAll()).thenReturn(discountList);

        List<Discount> actualDiscountsList = discountService.getAll();
        assertSame(actualDiscountsList, discountList);
        verify(discountRepositoryMock).getAll();
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionWhenDiscountDoesNotExist() throws Exception {
        when(discountRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());
        discountService.getById(5L);
    }

    @Test
    public void getByIdShouldReturnDiscountWhenDiscountExists() throws Exception {
        Discount discount = new Discount();
        when(discountRepositoryMock.getById(anyLong())).thenReturn(Optional.of(discount));

        Discount actualDiscount = discountService.getById(5L);
        assertSame(actualDiscount, discount);
        verify(discountRepositoryMock).getById(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void removeShouldThrowExceptionWhenDiscountDoesNotExist() throws Exception {
        when(discountRepositoryMock.existWithId(anyLong())).thenReturn(false);
        discountService.remove(5L);
    }

    @Test
    public void removeShouldRemoveDiscountWhenDiscountExists() throws Exception {
        when(discountRepositoryMock.existWithId(anyLong())).thenReturn(true);

        discountService.remove(5L);
        verify(discountRepositoryMock).existWithId(5L);
        verify(discountRepositoryMock).remove(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getAllByTariffPlanShouldThrowExceptionWhenTariffPlanDoesNotExist() throws Exception {
        when(tariffPlanServiceMock.existWithId(anyLong())).thenReturn(false);
        discountService.getAllByTariffPlan(5L);
    }

    @Test
    public void getAllByTariffPlanShouldReturnListOfDiscounts() throws Exception {
        List<Discount> discountList = Arrays.asList(mock(Discount.class), mock(Discount.class));
        when(tariffPlanServiceMock.existWithId(anyLong())).thenReturn(true);
        when(discountRepositoryMock.getByTariffPlanId(anyLong())).thenReturn(discountList);

        List<Discount> actualDiscountList = discountService.getAllByTariffPlan(5L);
        assertSame(actualDiscountList, discountList);
        verify(tariffPlanServiceMock).existWithId(5L);
        verify(discountRepositoryMock).getByTariffPlanId(5L);
    }

    private Discount createTestDiscount() {
        Discount discount = new Discount();
        discount.setName("test name");
        return discount;
    }
}
