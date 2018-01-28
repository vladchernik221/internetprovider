package test.com.chernik.internetprovider.servlet.command.account;

import com.chernik.internetprovider.service.AccountService;
import com.chernik.internetprovider.servlet.command.impl.account.AccountAddUsedTrafficCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.servlet.command.CommandUnitTest;

import static org.mockito.Mockito.*;

public class AccountAddUsedTrafficCommandPostUnitTest extends CommandUnitTest {
    private AccountAddUsedTrafficCommandPost command;
    private AccountService accountServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new AccountAddUsedTrafficCommandPost();
        accountServiceMock = mock(AccountService.class);
        command.setAccountService(accountServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(accountServiceMock);
        super.resetMocks();

        when(requestMock.getParameter("contractAnnexId")).thenReturn("5");
        when(requestMock.getParameter("usedTraffic")).thenReturn("100");
    }

    @Test
    public void executeShouldAddUsedTrafficWhenAllParametersAreCorrect() throws Exception {
        command.execute(requestMock, responseMock);
        verify(accountServiceMock).addUsedTraffic(5L, 100);
    }
}
