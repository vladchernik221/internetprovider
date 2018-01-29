package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.persistence.repository.UserRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.RepositoryIntegrationTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class UserRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private UserRepository userRepository;

    public UserRepositoryIntegrationTest() {
        super("UserRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        userRepository = contextInitializer.getComponent(UserRepository.class);
    }


    private User createTestExistUser() {
        Contract contract = new Contract();
        contract.setContractId(1L);

        User user = new User();
        user.setUserId(1L);
        user.setLogin("Login 1");
        user.setUserRole(UserRole.ADMIN);
        user.setBlocked(false);
        user.setContract(contract);
        return user;
    }

    private User createTestNewAdmin() {
        User user = new User();
        user.setLogin("Login 100");
        user.setPassword("Password 100");
        user.setUserRole(UserRole.ADMIN);
        user.setBlocked(false);
        return user;
    }

    private User createTestNewCustomer() {
        Contract contract = new Contract();
        contract.setContractId(1L);

        User user = new User();
        user.setLogin("Login 100");
        user.setPassword("Password 100");
        user.setUserRole(UserRole.CUSTOMER);
        user.setBlocked(false);
        user.setContract(contract);
        return user;
    }

    @Test
    public void existWithIdShouldReturnTrueWhenUserExists() throws Exception {
        assertTrue(userRepository.existWithId(1L));
    }

    @Test
    public void existWithIdShouldReturnFalseWhenUsernDoesNotExist() throws Exception {
        assertFalse(userRepository.existWithId(100L));
    }

    @Test
    public void existWithLoginShouldReturnTrueWhenUserExists() throws Exception {
        assertTrue(userRepository.existWithLogin("Login 1"));
    }

    @Test
    public void existWithLoginShouldReturnFalseWhenUserDoesNotExist() throws Exception {
        assertFalse(userRepository.existWithLogin("Wrong login"));
    }

    @Test
    public void getByIdShouldReturnUserWhenUserWithIdExists() throws Exception {
        User expected = createTestExistUser();
        User actual = userRepository.getById(1L).get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByIdShouldReturnNullWhenUserWithIdDoesExist() throws Exception {
        assertFalse(userRepository.getById(100L).isPresent());
    }

    @Test
    public void createShouldReturnGeneratedIdWhenUserWithNameDoesNotExist() throws Exception {
        Long actual = userRepository.create(createTestNewAdmin());
        assertNotNull(actual);
    }

    @Test
    public void createShouldSaveCreatedUserWithRoleAdmin() throws Exception {
        User expected = createTestNewAdmin();
        Long generatedId = userRepository.create(expected);
        expected.setUserId(generatedId);
        User actual = userRepository.getById(generatedId).get();
        expected.setPassword(null);

        assertEquals(actual, expected);
    }

    @Test
    public void createShouldSaveCreatedUserWithRoleSeller() throws Exception {
        User expected = createTestNewCustomer();
        Long generatedId = userRepository.create(expected);
        expected.setUserId(generatedId);
        User actual = userRepository.getById(generatedId).get();
        expected.setPassword(null);

        assertEquals(actual, expected);
    }

    @Test
    public void updatePasswordShouldSaveUpdatedUserPassword() throws Exception {
        User expected = createTestExistUser();
        userRepository.updatePassword(1L, "Password 2");
        User actual = userRepository.getByLoginAndPassword(expected.getLogin(), "Password 2").get();
        assertEquals(actual, expected);
    }

    @Test
    public void blockShouldBlockUserWhenUserNotBlocked() throws Exception {
        userRepository.block(1L);

        User blockedUser = userRepository.getById(1L).get();

        assertTrue(blockedUser.getBlocked());
    }

    @Test
    public void blockShouldUnBlockUserWhenUserBlocked() throws Exception {
        userRepository.block(2L);

        User blockedUser = userRepository.getById(2L).get();

        assertFalse(blockedUser.getBlocked());
    }

    @Test
    public void getByLoginAndPasswordShouldReturnUserWhenUserWithLoginAndPasswordExists() throws Exception {
        User expected = createTestExistUser();
        User actual = userRepository.getByLoginAndPassword("Login 1", "Password 1").get();
        assertEquals(actual, expected);
    }

    @Test
    public void getByLoginAndPasswordShouldReturnUserWhenUserWithLoginDoesNotExists() throws Exception {
        assertFalse(userRepository.getByLoginAndPassword("Wrong login", "Password 1").isPresent());
    }

    @Test
    public void getByLoginAndPasswordShouldReturnUserWhenPasswordWrong() throws Exception {
        assertFalse(userRepository.getByLoginAndPassword("Login 1", "Wrong password").isPresent());
    }

    @Test
    public void getPageShouldReturnPageOfUser() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);

        Page<User> actual = userRepository.getPage(pageable);
        List<Long> actualIds = actual.getData().stream().map(User::getUserId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageShouldReturnCorrectPageCount() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 5;

        Page<User> actual = userRepository.getPage(pageable);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void getPageWithRoleShouldReturnPageOfUserWhenRoleAdmin() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(1L, 4L, 7L);

        Page<User> actual = userRepository.getPageWithRole(pageable, UserRole.ADMIN);
        List<Long> actualIds = actual.getData().stream().map(User::getUserId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageWithRoleShouldReturnCorrectPageCountWhenRoleAdmin() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<User> actual = userRepository.getPageWithRole(pageable, UserRole.ADMIN);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void getPageWithRoleShouldReturnPageOfUserWhenRoleSeller() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(2L, 5L, 8L);

        Page<User> actual = userRepository.getPageWithRole(pageable, UserRole.SELLER);
        List<Long> actualIds = actual.getData().stream().map(User::getUserId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageWithRoleShouldReturnCorrectPageCountWhenRoleSeller() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<User> actual = userRepository.getPageWithRole(pageable, UserRole.SELLER);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }

    @Test
    public void getPageWithRoleShouldReturnPageOfUserWhenRoleCustomer() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(3);

        List<Long> expectedIds = Arrays.asList(3L, 6L, 9L);

        Page<User> actual = userRepository.getPageWithRole(pageable, UserRole.CUSTOMER);
        List<Long> actualIds = actual.getData().stream().map(User::getUserId).collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
    }

    @Test
    public void getPageWithRoleShouldReturnCorrectPageCountWhenRoleCustomer() throws Exception {
        Pageable pageable = new Pageable();
        pageable.setPageNumber(0);
        pageable.setPageSize(2);

        Integer expectedPageCount = 2;

        Page<User> actual = userRepository.getPageWithRole(pageable, UserRole.CUSTOMER);
        Integer actualPageCount = actual.getPagesCount();

        assertEquals(actualPageCount, expectedPageCount);
    }
}
