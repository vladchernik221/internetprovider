package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.impl.user.ChangePasswordCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import static org.mockito.Mockito.*;

public class ChangePasswordCommandPostUnitTest extends CommandUnitTest {
    private ChangePasswordCommandPost command;
    private UserService userServiceMock;
    private User testUser = new User();

    @BeforeClass
    public void init() {
        super.init();
        command = new ChangePasswordCommandPost();
        userServiceMock = mock(UserService.class);
        command.setUserService(userServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(userServiceMock);
        super.resetMocks();
        when(requestMock.getRequestURI()).thenReturn("/user/5/password");
        when(requestMock.getParameter("newPassword")).thenReturn("new password");
        when(sessionMock.getAttribute("user")).thenReturn(testUser);
    }

    @Test
    public void executeShouldChangePasswordViaService() throws Exception {
        command.execute(requestMock, responseMock);
        verify(userServiceMock).changePassword(5L, "new password", testUser);
    }
}
