package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.AccessDeniedException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.repository.AccountRepository;
import com.chernik.internetprovider.service.TransactionService;
import com.chernik.internetprovider.service.impl.AccountServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertSame;

public class AccountServiceUnitTest {
    private AccountServiceImpl accountService;

    private AccountRepository accountRepositoryMock;
    private TransactionService transactionServiceMock;

    @BeforeClass
    public void init() {
        accountService = new AccountServiceImpl();
        accountRepositoryMock = mock(AccountRepository.class);
        transactionServiceMock = mock(TransactionService.class);
        accountService.setAccountRepository(accountRepositoryMock);
        accountService.setTransactionService(transactionServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(accountRepositoryMock, transactionServiceMock);
    }

    private User createTestAdmin() {
        User user = new User();
        user.setUserId(1L);
        user.setUserRole(UserRole.ADMIN);
        return user;
    }

    private User createTestCustomer() {
        User user = new User();
        user.setUserId(1L);
        user.setUserRole(UserRole.CUSTOMER);
        return user;
    }

    @Test
    public void getByIdShouldReturnAccountIfItExists() throws Exception {
        Account account = new Account();
        when(accountRepositoryMock.getById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(true);

        Account actualAccount = accountService.getById(5L, 3, createTestCustomer());
        assertSame(actualAccount, account);
        verify(accountRepositoryMock, times(1)).getById(5L);
        verify(accountRepositoryMock, times(1)).isUserOwner(5L, 1L);
    }

    @Test(expectedExceptions = AccessDeniedException.class)
    public void getByIdShouldThrowExceptionIfUserHasRoleAdmin() throws Exception {
        accountService.getById(5L, 3, createTestAdmin());
    }

    @Test(expectedExceptions = AccessDeniedException.class)
    public void getByIdShouldThrowExceptionIfUserIsNotOwner() throws Exception {
        when(accountRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(false);
        accountService.getById(5L, 3, createTestAdmin());
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionWhenAccountDoesNotExist() throws Exception {
        when(accountRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());
        when(accountRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(true);

        accountService.getById(5L, 3, createTestCustomer());
    }

    @Test
    public void getByIdShouldGetPageOfAccountTransactions() throws Exception {
        Account account = new Account();
        when(accountRepositoryMock.getById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(true);

        Page<Transaction> transactionPage = new Page<>(Arrays.asList(mock(Transaction.class), mock(Transaction.class)), 8);
        when(transactionServiceMock.getPage(anyLong(), any(Pageable.class))).thenReturn(transactionPage);

        Account actualAccount = accountService.getById(5L, 3, createTestCustomer());

        assertSame(actualAccount.getTransactions(), transactionPage);
        verify(transactionServiceMock).getPage(5L, new Pageable(3, 10));
        verify(accountRepositoryMock, times(1)).isUserOwner(5L, 1L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void addUsedTrafficShouldThrowExceptionWhenAccountDoesNotExist() throws Exception {
        when(accountRepositoryMock.existWithId(anyLong())).thenReturn(false);

        accountService.addUsedTraffic(5L, 100);
    }

    @Test
    public void addUsedTrafficShouldAddUsedTrafficWhenAccountExists() throws Exception {
        when(accountRepositoryMock.existWithId(anyLong())).thenReturn(true);

        accountService.addUsedTraffic(5L, 100);
        verify(accountRepositoryMock).addUsedTraffic(5L, 100);
    }
}
