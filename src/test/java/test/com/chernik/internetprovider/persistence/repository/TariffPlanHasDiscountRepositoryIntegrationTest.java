package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.repository.DiscountRepository;
import com.chernik.internetprovider.persistence.repository.TariffPlanHasDiscountRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.util.RepositoryIntegrationTest;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class TariffPlanHasDiscountRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private TariffPlanHasDiscountRepository tariffPlanHasDiscountRepository;
    private DiscountRepository discountRepository;

    public TariffPlanHasDiscountRepositoryIntegrationTest() {
        super("TariffPlanHasDiscountRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        tariffPlanHasDiscountRepository = contextInitializer.getComponent(TariffPlanHasDiscountRepository.class);
        discountRepository = contextInitializer.getComponent(DiscountRepository.class);
    }


    private List<Discount> createTestNewDiscounts() throws ParseException {
        List<Discount> discounts = new ArrayList<>();

        Discount discount = new Discount();
        discount.setDiscountId(1L);
        discount.setName("Discount 1");
        discount.setDescription("Description 1");
        discount.setAmount(12);
        discount.setStartDate(Date.valueOf("2018-01-28"));
        discount.setEndDate(Date.valueOf("2018-01-28"));
        discount.setOnlyForNewClient(false);
        discounts.add(discount);

        discount = new Discount();
        discount.setDiscountId(2L);
        discount.setName("Discount 2");
        discount.setDescription("Description 2");
        discount.setAmount(32);
        discount.setStartDate(Date.valueOf("2018-01-28"));
        discount.setEndDate(Date.valueOf("2018-01-28"));
        discount.setOnlyForNewClient(false);
        discounts.add(discount);

        return discounts;
    }

    @Test
    public void existWithIdShouldReturnTrueWhenTariffPlanExists() throws Exception {
        assertTrue(tariffPlanHasDiscountRepository.existByTariffPlanId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenTariffPlanDoesNotExist() throws Exception {
        assertFalse(tariffPlanHasDiscountRepository.existByTariffPlanId(100L));
    }

    @Test
    public void createShouldSaveCreatedTariffPlanHasDiscount() throws Exception {
        List<Discount> expected = createTestNewDiscounts();
        tariffPlanHasDiscountRepository.create(2L, expected);
        List<Discount> actual = discountRepository.getByTariffPlanId(1L);

        assertEquals(actual, expected);
    }

    @Test
    public void removeShouldSaveRemoveTariffPlanHasDiscount() throws Exception {
        tariffPlanHasDiscountRepository.remove(1L);
        List<Discount> actual = discountRepository.getByTariffPlanId(1L);

        assertTrue(actual.isEmpty());
    }
}
