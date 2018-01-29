package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.repository.DiscountRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.RepositoryIntegrationTest;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class DiscountRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private DiscountRepository discountRepository;

    public DiscountRepositoryIntegrationTest() {
        super("DiscountRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        discountRepository = contextInitializer.getComponent(DiscountRepository.class);
    }


    private Discount createTestExistDiscount() {
        Discount discount = new Discount();
        discount.setDiscountId(1L);
        discount.setName("Discount 1");
        discount.setDescription("Description 1");
        discount.setAmount(12);
        discount.setStartDate(Date.valueOf("2018-01-28"));
        discount.setEndDate(Date.valueOf("2018-01-28"));
        discount.setOnlyForNewClient(false);
        return discount;
    }

    private Discount createTestNewDiscount() {
        Discount discount = new Discount();
        discount.setName("Discount 100");
        discount.setDescription("Description 100");
        discount.setAmount(12);
        discount.setStartDate(Date.valueOf("2018-01-28"));
        discount.setEndDate(Date.valueOf("2018-01-28"));
        discount.setOnlyForNewClient(false);
        return discount;
    }

    private Discount createTestUpdatedDiscount() {
        Discount discount = new Discount();
        discount.setDiscountId(1L);
        discount.setName("Discount 123");
        discount.setDescription("Description 123");
        discount.setAmount(24);
        discount.setStartDate(Date.valueOf("2018-01-28"));
        discount.setEndDate(Date.valueOf("2018-03-01"));
        discount.setOnlyForNewClient(true);
        return discount;
    }

    @Test
    public void existWithIdShouldReturnTrueWhenDiscountExists() throws Exception {
        assertTrue(discountRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenDiscountDoesNotExist() throws Exception {
        assertFalse(discountRepository.existWithId(100L));
    }

    @Test
    public void existWithNameShouldReturnTrueWhenDiscountExists() throws Exception {
        assertTrue(discountRepository.existWithName("Discount 1"));
    }

    @Test
    public void existWithNameShouldReturnFalseWhenDiscountDoesNotExist() throws Exception {
        assertFalse(discountRepository.existWithName("Wrong name"));
    }

    @Test
    public void existWithIdAndNameShouldReturnFalseWhenDiscountExists() throws Exception {
        assertFalse(discountRepository.existWithNameAndNotId(1L, "Discount 1"));
    }

    @Test
    public void existWithIdAndNameShouldReturnFalseWhenDiscountWithNameDoesNotExist() throws Exception {
        assertFalse(discountRepository.existWithNameAndNotId(1L, "Wrong name"));
    }

    @Test
    public void existWithIdAndNameShouldReturnTrueWhenDiscountWithIdDoesNotExist() throws Exception {
        assertTrue(discountRepository.existWithNameAndNotId(100L, "Discount 1"));
    }

    @Test
    public void getByIdShouldReturnDiscountWhenDiscountWithIdExists() throws Exception {
        Discount expected = createTestExistDiscount();
        Discount actual = discountRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenDiscountWithIdDoesExist() throws Exception {
        assertFalse(discountRepository.getById(100L).isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenDiscountWithNameDoesNotExist() throws Exception {
        Long actual = discountRepository.create(createTestNewDiscount());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedDiscount() throws Exception {
        Discount expected = createTestNewDiscount();
        Long generatedId = discountRepository.create(expected);
        expected.setDiscountId(generatedId);
        Discount actual = discountRepository.getById(generatedId).get();

        assertEquals(actual, expected);
    }

    @Test
    public void updateShouldSaveUpdatedDiscount() throws Exception {
        Discount expected = createTestUpdatedDiscount();
        discountRepository.update(expected);
        Discount actual = discountRepository.getById(expected.getDiscountId()).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByTariffPlanIdShouldAllDiscountWhichTariffPlanHave() throws Exception {
        List<Long> expectedIds = Arrays.asList(1L, 3L);

        List<Discount> actual = discountRepository.getByTariffPlanId(1L);
        List<Long> actualIds = actual.stream().map(Discount::getDiscountId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnPageOfDiscounts() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        Page<Discount> actual = discountRepository.getPage(pageable);
        List<Long> actualIds = actual.getData().stream().map(Discount::getDiscountId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<Discount> actual = discountRepository.getPage(pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void getAllShouldReturnAllDiscounts() throws Exception {
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        List<Discount> actual = discountRepository.getAll();
        List<Long> actualIds = actual.stream().map(Discount::getDiscountId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void removeShouldRemoveDiscount() throws Exception{
        boolean expected = discountRepository.existWithId(2L);
        discountRepository.remove(2L);
        boolean actual = discountRepository.existWithId(2L);

        assertNotEquals(actual, expected);
    }
}
