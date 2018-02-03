package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.ContractAnnexRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.RepositoryIntegrationTest;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class ContractAnnexRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private ContractAnnexRepository contractAnnexRepository;

    public ContractAnnexRepositoryIntegrationTest() {
        super("ContractAnnexRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        contractAnnexRepository = contextInitializer.getComponent(ContractAnnexRepository.class);
    }


    private ContractAnnex createTestExistContractAnnex() {
        Contract contract = new Contract();
        contract.setContractId(1L);

        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(1L);

        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(1L);
        contractAnnex.setContract(contract);
        contractAnnex.setAddress("Contract annex address 1");
        contractAnnex.setCanceled(false);
        contractAnnex.setConcludeDate(Date.valueOf("2018-01-28"));
        contractAnnex.setTariffPlan(tariffPlan);
        return contractAnnex;
    }

    private ContractAnnex createTestNewContract() {
        Contract contract = new Contract();
        contract.setContractId(1L);
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(1L);

        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContract(contract);
        contractAnnex.setAddress("Contract annex address 100");
        contractAnnex.setCanceled(false);
        contractAnnex.setTariffPlan(tariffPlan);
        return contractAnnex;
    }


    @Test
    public void existWithIdShouldReturnTrueWhenContractAnnexExists() throws Exception {
        assertTrue(contractAnnexRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenContractAnnexDoesNotExist() throws Exception {
        assertFalse(contractAnnexRepository.existWithId(100L));
    }

    @Test
    public void isCanceledShouldReturnTrueWhenContractAnnexCanceled() throws Exception {
        assertTrue(contractAnnexRepository.isCanceled(2L));
    }

    @Test
    public void isCanceledWithIdShouldReturnFalseWhenContractAnnexDoesNotCanceled() throws Exception {
        assertFalse(contractAnnexRepository.isCanceled(1L));
    }

    @Test
    public void getByIdShouldReturnContractAnnexWhenContractWithIdExists() throws Exception {
        ContractAnnex expected = createTestExistContractAnnex();
        ContractAnnex actual = contractAnnexRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenContractAnnexWithIdDoesExist() throws Exception {
        assertFalse(contractAnnexRepository.getById(100L).isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenContractWithNameDoesNotExist() throws Exception {
        Long actual = contractAnnexRepository.create(createTestNewContract());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedContract() throws Exception {
        ContractAnnex expected = createTestNewContract();
        Long generatedId = contractAnnexRepository.create(expected);
        expected.setContractAnnexId(generatedId);
        ContractAnnex actual = contractAnnexRepository.getById(generatedId).get();
        actual.setConcludeDate(null);

        assertEquals(actual, expected);
    }

    @Test
    public void getPageShouldReturnPageOfContractAnnex() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(2L, 3L, 4L);

        Page<ContractAnnex> actual = contractAnnexRepository.getPage(2L, pageable);
        List<Long> actualIds = actual.getData().stream().map(ContractAnnex::getContractAnnexId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<ContractAnnex> actual = contractAnnexRepository.getPage(2L, pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void canceledShouldCancelContractAnnexWhenContractAnnexNotCanceled() throws Exception {
        contractAnnexRepository.cancel(1L);

        ContractAnnex contractAnnex = contractAnnexRepository.getById(1L).get();

        assertTrue(contractAnnex.getCanceled());
    }


    @Test
    public void isUserOwnerShouldReturnTrueWhenUserIsOwnerOfContractAnnex() throws Exception {
        assertTrue(contractAnnexRepository.isUserOwner(1L, 1L));
    }

    @Test
    public void isUserOwnerWithIdShouldReturnFalseWhenUserIsNotOwnerOfContractAnnex() throws Exception {
        assertFalse(contractAnnexRepository.isUserOwner(1L, 100L));
    }
}
