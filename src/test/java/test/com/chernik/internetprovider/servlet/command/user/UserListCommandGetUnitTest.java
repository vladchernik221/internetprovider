package test.com.chernik.internetprovider.servlet.command.user;

import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.impl.user.UserListCommandGet;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class UserListCommandGetUnitTest extends CommandUnitTest {
    private UserListCommandGet command;
    private UserService userServiceMock;

    private Page<User> usersPage;

    @BeforeClass
    public void init() {
        super.init();
        command = new UserListCommandGet();
        userServiceMock = mock(UserService.class);
        command.setUserService(userServiceMock);
        command.setBaseMapper(getMapper(BaseMapper.class));

        usersPage = new Page<>(Arrays.asList(mock(User.class), mock(User.class)), 7);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        reset(userServiceMock);
        super.resetMocks();
        when(userServiceMock.getPage(any(Pageable.class), any(UserRole.class))).thenReturn(usersPage);
    }

    @Test
    public void executeShouldGetFirstUsersPageWhenPageIsNotSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn(null);
        when(requestMock.getParameter("role")).thenReturn("SELLER");

        command.execute(requestMock, responseMock);
        verify(userServiceMock).getPage(new Pageable(0, 10), UserRole.SELLER);
        verify(requestMock).setAttribute("usersPage", usersPage);
    }

    @Test
    public void executeShouldGetUsersPageWhenPageIsSpecified() throws Exception {
        when(requestMock.getParameter("page")).thenReturn("10");
        when(requestMock.getParameter("role")).thenReturn("CUSTOMER");

        command.execute(requestMock, responseMock);
        verify(userServiceMock).getPage( new Pageable(9, 10), UserRole.CUSTOMER);
        verify(requestMock).setAttribute("usersPage", usersPage);
    }
}
