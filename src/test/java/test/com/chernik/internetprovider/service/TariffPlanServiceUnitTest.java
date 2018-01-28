package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import com.chernik.internetprovider.service.DiscountService;
import com.chernik.internetprovider.service.TariffPlanDiscountService;
import com.chernik.internetprovider.service.impl.TariffPlanServiceImpl;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class TariffPlanServiceUnitTest {
    private TariffPlanServiceImpl tariffPlanService;

    private TariffPlanRepository tariffPlanRepositoryMock;
    private DiscountService discountServiceMock;
    private TariffPlanDiscountService tariffPlanDiscountServiceMock;

    private ArgumentCaptor<TariffPlan> tariffPlanCaptor = ArgumentCaptor.forClass(TariffPlan.class);

    @BeforeClass
    public void init() {
        tariffPlanService = new TariffPlanServiceImpl();
        tariffPlanRepositoryMock = mock(TariffPlanRepository.class);
        discountServiceMock = mock(DiscountService.class);
        tariffPlanDiscountServiceMock = mock(TariffPlanDiscountService.class);
        tariffPlanService.setTariffPlanRepository(tariffPlanRepositoryMock);
        tariffPlanService.setDiscountService(discountServiceMock);
        tariffPlanService.setTariffPlanDiscountService(tariffPlanDiscountServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(tariffPlanRepositoryMock, discountServiceMock, tariffPlanDiscountServiceMock);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void createShouldThrowExceptionWhenTariffPlanWithNameAlreadyExists() throws Exception {
        when(tariffPlanRepositoryMock.existWithName(anyString())).thenReturn(true);
        tariffPlanService.create(createTestTariffPlan());
    }

    @Test
    public void createShouldReturnGeneratedId() throws Exception {
        when(tariffPlanRepositoryMock.existWithName(anyString())).thenReturn(false);
        when(tariffPlanRepositoryMock.create(any(TariffPlan.class))).thenReturn(15L);

        TariffPlan tariffPlan = createTestTariffPlan();
        Long generatedId = tariffPlanService.create(tariffPlan);

        assertEquals(generatedId.longValue(), 15L);
        verify(tariffPlanRepositoryMock).existWithName("test name");
        verify(tariffPlanRepositoryMock).create(tariffPlan);
    }

    @Test
    public void createShouldCreateDiscountsForSavedTariffPlan() throws Exception {
        when(tariffPlanRepositoryMock.existWithName(anyString())).thenReturn(false);
        when(tariffPlanRepositoryMock.create(any(TariffPlan.class))).thenReturn(15L);

        Long generatedId = tariffPlanService.create(createTestTariffPlan());

        TariffPlan expectedTariffPlan = createTestTariffPlan();
        expectedTariffPlan.setTariffPlanId(generatedId);
        verify(tariffPlanDiscountServiceMock).create(tariffPlanCaptor.capture());
        assertEquals(tariffPlanCaptor.getValue(), expectedTariffPlan);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void updateShouldThrowExceptionWhenTariffPlanDoesNotExist() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(false);
        tariffPlanService.update(createTestTariffPlan());
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void updateShouldThrowExceptionWhenOtherTariffPlanWithNameAlreadyExists() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(tariffPlanRepositoryMock.existWithNameAndNotId(anyLong(), anyString())).thenReturn(true);
        tariffPlanService.update(createTestTariffPlan());
    }

    @Test
    public void updateShouldSaveUpdatedTariffPlanWithDiscounts() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(tariffPlanRepositoryMock.existWithNameAndNotId(anyLong(), anyString())).thenReturn(false);

        TariffPlan tariffPlan = createTestTariffPlan();
        tariffPlanService.update(tariffPlan);
        verify(tariffPlanRepositoryMock).existWithId(5L);
        verify(tariffPlanRepositoryMock).existWithNameAndNotId(5L, "test name");
        verify(tariffPlanRepositoryMock).update(tariffPlan);
        verify(tariffPlanDiscountServiceMock).update(tariffPlan);
    }

    @Test
    public void getPageShouldReturnPageOfTariffPlans() throws Exception {
        Page<TariffPlan> page = new Page<>(Arrays.asList(mock(TariffPlan.class), mock(TariffPlan.class)), 5);
        when(tariffPlanRepositoryMock.getPage(anyBoolean(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<TariffPlan> actualPage = tariffPlanService.getPage(pageable, true);
        assertSame(actualPage, page);
        verify(tariffPlanRepositoryMock).getPage(true, pageable);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionWhenTariffPlanDoesNotExist() throws Exception {
        when(tariffPlanRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());
        tariffPlanService.getById(5L);
    }

    @Test
    public void getByIdShouldReturnTariffPlanWhenTariffPlanExists() throws Exception {
        TariffPlan tariffPlan = createTestTariffPlan();
        when(tariffPlanRepositoryMock.getById(anyLong())).thenReturn(Optional.of(tariffPlan));

        TariffPlan actualTariffPlan = tariffPlanService.getById(5L);
        assertSame(actualTariffPlan, tariffPlan);
        verify(tariffPlanRepositoryMock).getById(5L);
    }

    @Test
    public void getByIdShouldReturnTariffPlanWithDiscounts() throws Exception {
        List<Discount> discounts = Arrays.asList(mock(Discount.class), mock(Discount.class));
        when(tariffPlanRepositoryMock.getById(anyLong())).thenReturn(Optional.of(createTestTariffPlan()));
        when(discountServiceMock.getAllByTariffPlan(anyLong())).thenReturn(discounts);

        TariffPlan actualTariffPlan = tariffPlanService.getById(5L);
        assertSame(actualTariffPlan.getDiscounts(), discounts);
        verify(discountServiceMock).getAllByTariffPlan(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void archiveShouldThrowExceptionWhenTariffPlanDoesNotExist() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(false);
        tariffPlanService.archive(5L);
    }

    @Test
    public void archiveShouldChangeStatusWhenTariffPlanExists() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(true);

        tariffPlanService.archive(5L);
        verify(tariffPlanRepositoryMock).existWithId(5L);
        verify(tariffPlanRepositoryMock).archive(5L);
    }

    @Test
    public void getAllNotArchivedShouldReturnListOfNotArchivedTariffPlans() throws Exception {
        List<TariffPlan> tariffPlanList = Arrays.asList(mock(TariffPlan.class), mock(TariffPlan.class));
        when(tariffPlanRepositoryMock.getAllNotArchived()).thenReturn(tariffPlanList);

        List<TariffPlan> actualTariffPlanList = tariffPlanService.getAllNotArchived();
        assertSame(actualTariffPlanList, tariffPlanList);
        verify(tariffPlanRepositoryMock).getAllNotArchived();
    }

    @Test
    public void existWithIdShouldReturnTrueWhenTariffPlanExists() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(true);

        Boolean result = tariffPlanService.existWithId(5L);
        assertTrue(result);
        verify(tariffPlanRepositoryMock).existWithId(5L);
    }

    @Test
    public void existWithIdShouldReturnFalseWhenTariffPlanDoesNotExist() throws Exception {
        when(tariffPlanRepositoryMock.existWithId(anyLong())).thenReturn(false);

        Boolean result = tariffPlanService.existWithId(5L);
        assertFalse(result);
        verify(tariffPlanRepositoryMock).existWithId(5L);
    }

    private TariffPlan createTestTariffPlan() {
        TariffPlan tariffPlan = new TariffPlan(5L);
        tariffPlan.setName("test name");
        return tariffPlan;
    }
}
