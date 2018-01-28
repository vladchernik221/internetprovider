package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.TariffPlanRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.util.RepositoryIntegrationTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class TariffPlanRepositoryIntegrationTest extends RepositoryIntegrationTest {
    private TariffPlanRepository tariffPlanRepository;

    public TariffPlanRepositoryIntegrationTest() {
        super("TariffPlanRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        tariffPlanRepository = contextInitializer.getComponent(TariffPlanRepository.class);
    }

    private TariffPlan createTestExistTariffPlan() {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(1L);
        tariffPlan.setName("Tariff plan 1");
        tariffPlan.setDescription("Description for tariff plan 1");
        tariffPlan.setDownSpeed(12);
        tariffPlan.setUpSpeed(6);
        tariffPlan.setIncludedTraffic(1000);
        tariffPlan.setPriceOverTraffic(BigDecimal.valueOf(0.45));
        tariffPlan.setMonthlyFee(BigDecimal.valueOf(12.99));
        tariffPlan.setArchived(false);
        return tariffPlan;
    }

    private TariffPlan createTestNewTariffPlan() {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setName("Tariff plan 100");
        tariffPlan.setDescription("Description for tariff plan 100");
        tariffPlan.setDownSpeed(12);
        tariffPlan.setUpSpeed(6);
        tariffPlan.setMonthlyFee(BigDecimal.valueOf(12.99));
        tariffPlan.setArchived(false);
        return tariffPlan;
    }

    private TariffPlan createTestUpdatedTariffPlan() {
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(1L);
        tariffPlan.setName("Updated tariff plan 1");
        tariffPlan.setDescription("Updates description for tariff plan 1");
        tariffPlan.setDownSpeed(18);
        tariffPlan.setUpSpeed(9);
        tariffPlan.setIncludedTraffic(2000);
        tariffPlan.setPriceOverTraffic(new BigDecimal("0.90"));
        tariffPlan.setMonthlyFee(BigDecimal.valueOf(24.99));
        tariffPlan.setArchived(false);
        return tariffPlan;
    }

    @Test
    public void existWithIdShouldReturnTrueWhenTariffPlanExists() throws Exception {
        assertTrue(tariffPlanRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenTariffPlanDoesNotExist() throws Exception {
        assertFalse(tariffPlanRepository.existWithId(100L));
    }

    @Test
    public void existWithNameShouldReturnTrueWhenTariffPlanExists() throws Exception {
        assertTrue(tariffPlanRepository.existWithName("Tariff plan 1"));
    }

    @Test
    public void existWithNameShouldReturnFalseWhenTariffPlanDoesNotExist() throws Exception {
        assertFalse(tariffPlanRepository.existWithName("Wrong name"));
    }

    @Test
    public void existWithIdAndNameShouldReturnFalseWhenTariffPlanExists() throws Exception {
        assertFalse(tariffPlanRepository.existWithNameAndNotId(1L, "Tariff plan 1"));
    }

    @Test
    public void existWithIdAndNameShouldReturnFalseWhenTariffPlanWithNameDoesNotExist() throws Exception {
        assertFalse(tariffPlanRepository.existWithNameAndNotId(1L, "Wrong name"));
    }

    @Test
    public void existWithIdAndNameShouldReturnTrueWhenTariffPlanWithIdDoesNotExist() throws Exception {
        assertTrue(tariffPlanRepository.existWithNameAndNotId(100L, "Tariff plan 1"));
    }

    @Test
    public void getByIdShouldReturnTariffPlanWhenTariffPlanWithIdExists() throws Exception {
        TariffPlan expected = createTestExistTariffPlan();
        TariffPlan actual = tariffPlanRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenTariffPlanWithIdDoesExist() throws Exception {
        assertFalse(tariffPlanRepository.getById(100L).isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenTariffPlanWithNameDoesNotExist() throws Exception {
        Long actual = tariffPlanRepository.create(createTestNewTariffPlan());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedTariffPlan() throws Exception {
        TariffPlan expected = createTestNewTariffPlan();
        Long generatedId = tariffPlanRepository.create(expected);
        expected.setTariffPlanId(generatedId);
        TariffPlan actual = tariffPlanRepository.getById(generatedId).get();

        assertEquals(actual, expected);
    }

    @Test
    public void updateShouldSaveUpdatedTariffPlan() throws Exception {
        TariffPlan expected = createTestUpdatedTariffPlan();
        tariffPlanRepository.update(expected);
        TariffPlan actual = tariffPlanRepository.getById(expected.getTariffPlanId()).get();
        assertEquals(actual, expected);
    }

    @Test
    public void archiveShouldArchiveTariffPlanWhenTariffPlanNotArchived() throws Exception {
        tariffPlanRepository.archive(1L);

        TariffPlan archivedTariffPlan = tariffPlanRepository.getById(1L).get();

        assertTrue(archivedTariffPlan.getArchived());
    }

    @Test
    public void archiveShouldUnArchivedTariffPlanWhenTariffPlanArchived() throws Exception {
        tariffPlanRepository.archive(2L);

        TariffPlan archivedTariffPlan = tariffPlanRepository.getById(2L).get();

        assertFalse(archivedTariffPlan.getArchived());
    }

    @Test
    public void getPageShouldReturnPageOfTariffPlansWithArchived() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        Integer expectedPageCount = 1;
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        Page<TariffPlan> actual = tariffPlanRepository.getPage(true, pageable);
        Integer actualPageCount = actual.getPagesCount();
        List<Long> actualIds = actual.getData().stream().map(TariffPlan::getTariffPlanId).collect(Collectors.toList());

        assertEquals(actualPageCount, expectedPageCount);
        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnPageOfTariffPlansWithoutArchived() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(1L, 3L);

        Page<TariffPlan> actual = tariffPlanRepository.getPage(false, pageable);
        List<Long> actualIds = actual.getData().stream().map(TariffPlan::getTariffPlanId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<TariffPlan> actual = tariffPlanRepository.getPage(true, pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void getAllNotArchivedShouldReturnListOfNotArchivedPlan() throws Exception {
        List<Long> expectedIds = Arrays.asList(1L, 3L);
        List<TariffPlan> tariffPlans = tariffPlanRepository.getAllNotArchived();

        List<Long> actualIds = tariffPlans.stream().map(TariffPlan::getTariffPlanId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }
}
