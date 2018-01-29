package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.impl.user.LoginCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class LoginCommandPostUnitTest extends CommandUnitTest {
    private LoginCommandPost command;
    private UserService userServiceMock;

    private HttpSession sessionMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new LoginCommandPost();
        userServiceMock = mock(UserService.class);
        command.setUserService(userServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        sessionMock = mock(HttpSession.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(userServiceMock);
        super.resetMocks();
        when(requestMock.getParameter("login")).thenReturn("test login");
        when(requestMock.getParameter("password")).thenReturn("password");
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(responseMock.getWriter()).thenReturn(mock(PrintWriter.class));
    }

    @Test
    public void executeShouldAuthorizeUserWhenExistAndNotBlocked() throws Exception {
        User user = createTestUser(false);
        when(userServiceMock.authenticate(anyString(), anyString())).thenReturn(Optional.of(user));

        command.execute(requestMock, responseMock);
        verify(userServiceMock).authenticate("test login", "password");
        verify(sessionMock).setAttribute("user", user);
    }

    @Test
    public void executeShouldSetUnauthorizeStatusWhenUserDoesNotExist() throws Exception {
        when(userServiceMock.authenticate(anyString(), anyString())).thenReturn(Optional.empty());

        command.execute(requestMock, responseMock);
        verify(userServiceMock).authenticate("test login", "password");
        verify(responseMock).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void executeShouldSetUnauthorizeStatusWhenUserIsBlocked() throws Exception {
        when(userServiceMock.authenticate(anyString(), anyString())).thenReturn(Optional.of(createTestUser(true)));

        command.execute(requestMock, responseMock);
        verify(userServiceMock).authenticate("test login", "password");
        verify(responseMock).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private User createTestUser(boolean blocked) {
        User user = new User();
        user.setBlocked(blocked);
        return user;
    }
}
