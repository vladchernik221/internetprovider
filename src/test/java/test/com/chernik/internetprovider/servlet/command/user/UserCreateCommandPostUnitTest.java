package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.impl.user.UserCreateCommandPost;
import com.chernik.internetprovider.servlet.mapper.UserMapper;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class UserCreateCommandPostUnitTest extends CommandUnitTest {
    private UserCreateCommandPost command;
    private UserService userServiceMock;

    private PrintWriter printWriterMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new UserCreateCommandPost();
        userServiceMock = mock(UserService.class);
        command.setUserService(userServiceMock);
        command.setUserMapper(getMapper(UserMapper.class));

        printWriterMock = mock(PrintWriter.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(userServiceMock, printWriterMock);
        super.resetMocks();
        when(requestMock.getParameter("login")).thenReturn("test login");
        when(requestMock.getParameter("password")).thenReturn("test password");
        when(requestMock.getParameter("userRole")).thenReturn("ADMIN");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(userServiceMock.create(any(User.class))).thenReturn(5L);
    }

    @Test
    public void executeShouldSaveUserViaService() throws Exception {
        command.execute(requestMock, responseMock);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userServiceMock).create(captor.capture());
        assertEquals(captor.getValue(), createTestUser());
    }

    @Test
    public void executeShouldReturnGeneratedId() throws Exception {
        command.execute(requestMock, responseMock);
        verify(printWriterMock).write("5");
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("test login");
        user.setPassword("test password");
        user.setUserRole(UserRole.ADMIN);
        return user;
    }
}
