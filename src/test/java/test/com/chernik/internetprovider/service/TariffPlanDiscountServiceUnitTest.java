package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanHasDiscountRepository;
import com.chernik.internetprovider.service.impl.TariffPlanDiscountServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class TariffPlanDiscountServiceUnitTest {
    private TariffPlanDiscountServiceImpl tariffPlanDiscountService;
    private TariffPlanHasDiscountRepository tariffPlanHasDiscountRepositoryMock;

    @BeforeClass
    public void init() {
        tariffPlanDiscountService = spy(new TariffPlanDiscountServiceImpl());
        tariffPlanHasDiscountRepositoryMock = mock(TariffPlanHasDiscountRepository.class);
        tariffPlanDiscountService.setTariffPlanHasDiscountRepository(tariffPlanHasDiscountRepositoryMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(tariffPlanDiscountService, tariffPlanHasDiscountRepositoryMock);
    }

    @Test
    public void createShouldDoNothingWhenTariffPlanDoesNotHaveDiscounts() throws Exception {
        List<Discount> discounts = Collections.emptyList();
        tariffPlanDiscountService.create(createTestTariffPlan(discounts));
        verify(tariffPlanHasDiscountRepositoryMock, never()).create(5L, discounts);
    }

    @Test
    public void createShouldSaveDiscountsForTariffPlanWhenTariffPlanHasDiscounts() throws Exception {
        List<Discount> discounts = Arrays.asList(mock(Discount.class), mock(Discount.class));
        tariffPlanDiscountService.create(createTestTariffPlan(discounts));
        verify(tariffPlanHasDiscountRepositoryMock).create(5L, discounts);
    }

    @Test
    public void updateShouldOnlyCreateDiscountsForTariffPlanWhenTariffPlanDoesNotHaveDiscounts() throws Exception {
        when(tariffPlanHasDiscountRepositoryMock.existByTariffPlanId(5L)).thenReturn(false);
        doNothing().when(tariffPlanDiscountService).create(any(TariffPlan.class));

        TariffPlan tariffPlan = new TariffPlan(5L);
        tariffPlanDiscountService.update(tariffPlan);

        verify(tariffPlanHasDiscountRepositoryMock, never()).remove(5L);
        verify(tariffPlanDiscountService).create(tariffPlan);
    }

    @Test
    public void updateShouldRemoveAndCreateDiscountsForTariffPlanWhenTariffPlanHasDiscounts() throws Exception {
        when(tariffPlanHasDiscountRepositoryMock.existByTariffPlanId(5L)).thenReturn(true);
        doNothing().when(tariffPlanDiscountService).create(any(TariffPlan.class));

        TariffPlan tariffPlan = new TariffPlan(5L);
        tariffPlanDiscountService.update(tariffPlan);

        verify(tariffPlanHasDiscountRepositoryMock).remove(5L);
        verify(tariffPlanDiscountService).create(tariffPlan);
    }

    private TariffPlan createTestTariffPlan(List<Discount> discounts) {
        TariffPlan tariffPlan = new TariffPlan(5L);
        tariffPlan.setDiscounts(discounts);
        return tariffPlan;
    }
}
