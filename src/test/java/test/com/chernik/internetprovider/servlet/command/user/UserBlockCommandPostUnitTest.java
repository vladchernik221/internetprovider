package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.impl.user.UserBlockCommandPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class UserBlockCommandPostUnitTest extends CommandUnitTest {
    private UserBlockCommandPost command;
    private UserService userServiceMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new UserBlockCommandPost();
        userServiceMock = mock(UserService.class);
        command.setUserService(userServiceMock);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(userServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/user/5/block");
    }

    @Test
    public void executeShouldUpdateUserStatusViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(userServiceMock).block(5L);
    }
}
