package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.repository.TransactionRepository;
import com.chernik.internetprovider.service.ContractAnnexService;
import com.chernik.internetprovider.service.impl.TransactionServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertSame;

public class TransactionServiceUnitTest {
    private TransactionServiceImpl transactionService;

    private TransactionRepository transactionRepositoryMock;
    private ContractAnnexService contractAnnexServiceMock;

    @BeforeClass
    public void init() {
        transactionService = new TransactionServiceImpl();
        transactionRepositoryMock = mock(TransactionRepository.class);
        contractAnnexServiceMock = mock(ContractAnnexService.class);
        transactionService.setTransactionRepository(transactionRepositoryMock);
        transactionService.setContractAnnexService(contractAnnexServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(transactionRepositoryMock, contractAnnexServiceMock);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void createShouldThrowExceptionWhenContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(false);
        transactionService.create(createTestTransaction());
    }

    @Test
    public void createShouldSaveTransactionWhenContractAnnexExists() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(true);

        Transaction transaction = createTestTransaction();
        transactionService.create(transaction);

        verify(contractAnnexServiceMock).existById(10L);
        verify(transactionRepositoryMock).create(transaction);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getPageShouldThrowExceptionWhenContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(false);
        transactionService.getPage(10L, new Pageable(3, 10));
    }

    @Test
    public void getPageShouldReturnPageOfTransactions() throws Exception {
        Page<Transaction> page = new Page<>(Arrays.asList(mock(Transaction.class), mock(Transaction.class)), 5);
        when(contractAnnexServiceMock.existById(anyLong())).thenReturn(true);
        when(transactionRepositoryMock.getPage(anyLong(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<Transaction> actualPage = transactionService.getPage(10L, pageable);

        assertSame(actualPage, page);
        verify(contractAnnexServiceMock).existById(10L);
        verify(transactionRepositoryMock).getPage(10L, pageable);
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        Account account = new Account();
        account.setContractAnnex(new ContractAnnex(10L));
        transaction.setAccount(account);
        return transaction;
    }
}
