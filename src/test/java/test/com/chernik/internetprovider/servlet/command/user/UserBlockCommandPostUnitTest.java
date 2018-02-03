package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
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
    private User testUser;

    @BeforeClass
    public void init() {
        super.init();
        command = new UserBlockCommandPost();
        userServiceMock = mock(UserService.class);
        command.setUserService(userServiceMock);
        testUser = createTestUser();
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(userServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/user/5/block");
        when(sessionMock.getAttribute("user")).thenReturn(testUser);
    }

    private User createTestUser() {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        return user;
    }

    @Test
    public void executeShouldUpdateUserStatusViaService() throws Exception {
        testUser.setUserId(1L);
        command.execute(requestMock, responseMock);
        verify(userServiceMock).block(5L);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void executeShouldThrowExceptionWhenIdEqualsAuthorizedUser() throws Exception {
        testUser.setUserId(5L);
        command.execute(requestMock, responseMock);
        verify(userServiceMock).block(5L);
    }
}
