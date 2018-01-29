package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.servlet.command.impl.user.LogoutCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class LogoutCommandPostUnitTest extends CommandUnitTest {
    private LogoutCommandPost command;
    private HttpSession sessionMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new LogoutCommandPost();
        sessionMock = mock(HttpSession.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        super.resetMocks();
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    public void executeShouldRemoveUserFromSession() throws Exception {
        command.execute(requestMock, responseMock);
        verify(sessionMock).removeAttribute("user");
    }
}
