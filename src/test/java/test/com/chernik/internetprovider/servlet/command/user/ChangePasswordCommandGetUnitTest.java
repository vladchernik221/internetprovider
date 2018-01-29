package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.servlet.command.impl.user.ChangePasswordCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangePasswordCommandGetUnitTest extends CommandUnitTest {
    private ChangePasswordCommandGet command = new ChangePasswordCommandGet();

    @BeforeClass
    public void init() {
        super.init();
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/user/5/password");
    }

    @Test
    public void executeShouldReturnPageWithService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(requestMock).setAttribute("userId", 5L);
    }
}
