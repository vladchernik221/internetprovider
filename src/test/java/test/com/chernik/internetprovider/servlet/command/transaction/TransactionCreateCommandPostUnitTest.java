package test.com.chernik.internetprovider.servlet.command.transaction;

import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.entity.TransactionType;
import com.chernik.internetprovider.service.TransactionService;
import com.chernik.internetprovider.servlet.command.impl.transaction.TransactionCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.TransactionMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.io.PrintWriter;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class TransactionCreateCommandPostUnitTest extends CommandUnitTest {
    private TransactionCreateCommandPost command;
    private TransactionService transactionServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new TransactionCreateCommandPost();
        transactionServiceMock = mock(TransactionService.class);
        command.setTransactionService(transactionServiceMock);
        command.setTransactionMapper(getMapper(TransactionMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(transactionServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getParameter("amount")).thenReturn("100.99");
        when(requestMock.getParameter("contractAnnexId")).thenReturn("10");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
    }

    @Test
    public void executeShouldSaveTransactionViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionServiceMock).create(captor.capture());
        assertEquals(captor.getValue(), createTestTransaction());
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.REFILL);
        transaction.setAmount(new BigDecimal("100.99"));
        Account account = new Account();
        account.setContractAnnex(new ContractAnnex(10L));
        transaction.setAccount(account);
        return transaction;
    }
}
