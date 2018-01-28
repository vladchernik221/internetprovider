package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import com.chernik.internetprovider.service.impl.UserServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

public class UserServiceUnitTest {
    private UserServiceImpl userService;
    private UserRepository userRepositoryMock;

    @BeforeClass
    public void init() {
        userService = new UserServiceImpl();
        userRepositoryMock = mock(UserRepository.class);
        userService.setUserRepository(userRepositoryMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(userRepositoryMock);
    }

    @Test
    public void authenticateShouldReturnUserOptinalByLoginAndPassword() throws Exception {
        Optional<User> user = Optional.of(createTestUser());
        when(userRepositoryMock.getByLoginAndPassword(anyString(), anyString())).thenReturn(user);

        Optional<User> actualUser = userService.authenticate("test login", "test password");
        assertSame(actualUser, user);
        verify(userRepositoryMock).getByLoginAndPassword("test login", "test password");
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void createShouldThrowExceptionWhenUserWithLoginAlreadyExists() throws Exception {
        when(userRepositoryMock.existWithLogin(anyString())).thenReturn(true);
        userService.create(createTestUser());
    }

    @Test
    public void createShouldReturnGeneratedId() throws Exception {
        when(userRepositoryMock.existWithLogin(anyString())).thenReturn(false);
        when(userRepositoryMock.create(any(User.class))).thenReturn(5L);

        User user = createTestUser();
        Long generatedId = userService.create(user);

        assertEquals(generatedId.longValue(), 5L);
        verify(userRepositoryMock).existWithLogin("test login");
        verify(userRepositoryMock).create(user);
    }

    @Test
    public void getPageShouldReturnPageOfAllUsersWhenRoleIsEmpty() throws Exception {
        Page<User> page = new Page<>(Arrays.asList(mock(User.class), mock(User.class)), 5);
        when(userRepositoryMock.getPage(any(Pageable.class))).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<User> actualPage = userService.getPage(pageable, null);
        assertSame(actualPage, page);
        verify(userRepositoryMock).getPage(pageable);
    }

    @Test
    public void getPageShouldReturnPageOfUsersWithRoleWhenRoleIsNotEmpty() throws Exception {
        Page<User> page = new Page<>(Arrays.asList(mock(User.class), mock(User.class)), 5);
        when(userRepositoryMock.getPageWithRole(any(Pageable.class), any())).thenReturn(page);

        Pageable pageable = new Pageable(3, 10);
        Page<User> actualPage = userService.getPage(pageable, UserRole.ADMIN);
        assertSame(actualPage, page);
        verify(userRepositoryMock).getPageWithRole(pageable, UserRole.ADMIN);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void blockShouldThrowExceptionWhenUserDoesNotExist() throws Exception {
        when(userRepositoryMock.existWithId(anyLong())).thenReturn(false);
        userService.block(5L);
    }

    @Test
    public void blockShouldChangeStatusWhenUserExists() throws Exception {
        when(userRepositoryMock.existWithId(anyLong())).thenReturn(true);

        userService.block(5L);
        verify(userRepositoryMock).existWithId(5L);
        verify(userRepositoryMock).block(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void changePasswordShouldThrowExceptionWhenUserDoesNotExist() throws Exception {
        when(userRepositoryMock.existWithId(anyLong())).thenReturn(false);
        userService.changePassword(5L, "new test password");
    }

    @Test
    public void changePasswordShouldSaveNewPasswordWhenUserExists() throws Exception {
        when(userRepositoryMock.existWithId(anyLong())).thenReturn(true);

        userService.changePassword(5L, "new test password");
        verify(userRepositoryMock).existWithId(5L);
        verify(userRepositoryMock).updatePassword(5L, "new test password");
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("test login");
        return user;
    }
}
