package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.exception.AccessDeniedException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.impl.user.ChangePasswordCommandGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangePasswordCommandGetUnitTest extends CommandUnitTest {
    private ChangePasswordCommandGet command = new ChangePasswordCommandGet();
    private User testUser;

    @BeforeClass
    public void init() {
        super.init();
        testUser = createTestUser();
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/user/5/password");
        when(sessionMock.getAttribute("user")).thenReturn(testUser);
    }

    private User createTestUser() {
        User user = new User();
        user.setUserRole(UserRole.CUSTOMER);
        return user;
    }

    @Test
    public void executeShouldReturnPageWithService() throws Exception {
        testUser.setUserId(5L);
        command.execute(requestMock, responseMock);
        verify(requestMock).setAttribute("userId", 5L);
    }

    @Test(expectedExceptions = AccessDeniedException.class)
    public void executeShouldThrowExceptionWenAccessDenied() throws Exception {
        testUser.setUserId(1L);
        command.execute(requestMock, responseMock);
        verify(requestMock).setAttribute("userId", 5L);
    }
}
