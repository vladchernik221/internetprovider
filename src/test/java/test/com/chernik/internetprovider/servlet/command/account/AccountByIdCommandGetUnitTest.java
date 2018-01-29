package test.com.chernik.internetprovider.servlet.command.account;

import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.servlet.command.impl.account.AccountByIdCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class AccountByIdCommandGetUnitTest extends CommandUnitTest {
    private AccountByIdCommandGet command;
    private AccountService accountServiceMock;

    private Account account;

    @BeforeClass
    public void init() {
        super.init();
        command = new AccountByIdCommandGet();
        accountServiceMock = mock(AccountService.class);
        command.setAccountService(accountServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        account = mock(Account.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(accountServiceMock, account);

        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/contract/annex/5/account");
        when(requestMock.getParameter("page")).thenReturn("10");
        when(accountServiceMock.getById(anyLong(), anyInt())).thenReturn(account);
    }

    @Test
    public void executeShouldGetAccountWithFirstTransactionsPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);

        command.execute(requestMock, responseMock);
        verify(accountServiceMock).getById(5L, 0);
        verify(requestMock).setAttribute("account", account);
    }

    @Test
    public void executeShouldGetAccountWithSpecifiedTransactionsPageWhenPageIsSpecified() throws Exception {
        command.execute(requestMock, responseMock);
        verify(accountServiceMock).getById(5L, 9);
        verify(requestMock).setAttribute("account", account);
    }
}
