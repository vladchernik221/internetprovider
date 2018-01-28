package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.*;
import com.chernik.internetprovider.persistence.repository.TransactionRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.util.RepositoryIntegrationTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class TransactionRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private TransactionRepository transactionRepository;

    public TransactionRepositoryIntegrationTest() {
        super("TransactionRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        transactionRepository = contextInitializer.getComponent(TransactionRepository.class);
    }


    private Transaction createTestNewTransaction() {
        Contract contract = new Contract();
        contract.setContractId(1L);

        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(1L);

        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(1L);
        contractAnnex.setContract(contract);
        contractAnnex.setTariffPlan(tariffPlan);

        Account account = new Account();
        account.setContractAnnex(contractAnnex);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new BigDecimal("123.12"));
        transaction.setType(TransactionType.REFILL);
        return transaction;
    }

    @Test
    public void createShouldSaveCreatedTransaction() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(1000);

        Transaction transaction = createTestNewTransaction();
        transactionRepository.create(transaction);
        Page<Transaction> transactionPage = transactionRepository.getPage(1L, pageable);

        Transaction actual = transactionPage.getData().get(transactionPage.getData().size() - 1);
        Transaction expected = createTestNewTransaction();
        actual.setDate(null);
        expected.setAccount(null);
        expected.setTransactionId(actual.getTransactionId());

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void getPageShouldReturnPageOfTransaction() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        Page<Transaction> actual = transactionRepository.getPage(1L, pageable);
        List<Long> actualIds = actual.getData().stream().map(Transaction::getTransactionId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<Transaction> actual = transactionRepository.getPage(1L, pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }
}
