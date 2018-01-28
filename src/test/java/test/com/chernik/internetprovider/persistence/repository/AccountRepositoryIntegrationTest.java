package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.repository.AccountRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.util.RepositoryIntegrationTest;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class AccountRepositoryIntegrationTest extends RepositoryIntegrationTest {
    private AccountRepository accountRepository;

    public AccountRepositoryIntegrationTest() {
        super("AccountRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        accountRepository = contextInitializer.getComponent(AccountRepository.class);
    }

    private Account createTestExistAccount() {
        Account account = new Account();
        account.setBalance(new BigDecimal("0.00"));
        account.setUsedTraffic(0);
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(1L);
        account.setContractAnnex(contractAnnex);
        return account;
    }

    private Account createTestUpdatedAccount() {
        Account account = new Account();
        account.setBalance(new BigDecimal("0.00"));
        account.setUsedTraffic(150);
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(1L);
        account.setContractAnnex(contractAnnex);
        return account;
    }

    @Test
    public void existWithIdShouldReturnTrueWhenAccountExists() throws Exception {
        assertTrue(accountRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnTrueWhenAccountNotExists() throws Exception {
        assertFalse(accountRepository.existWithId(100L));
    }

    @Test
    public void getByIdShouldReturnTariffPlanWhenTariffPlanWithIdExists() throws Exception {
        Account expected = createTestExistAccount();
        Account actual = accountRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenTariffPlanWithIdDoesExist() throws Exception {
        assertFalse(accountRepository.getById(100L).isPresent());
    }

    @Test
    public void addUsedTrafficShouldPlusUsedTrafficToAccountBalance() throws Exception {
        Account expected = createTestUpdatedAccount();
        accountRepository.addUsedTraffic(1L, 150);
        Account actual = accountRepository.getById(1L).get();
        assertEquals(actual, expected);
    }
}
