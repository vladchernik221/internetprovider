package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.AccessDeniedException;
import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.*;
import com.chernik.internetprovider.persistence.repository.ContractRepository;
import com.chernik.internetprovider.service.IndividualClientInformationService;
import com.chernik.internetprovider.service.LegalEntityClientInformationService;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.service.impl.ContractServiceImpl;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ContractServiceUnitTest {
    private ContractServiceImpl contractService;

    private ContractRepository contractRepositoryMock;
    private IndividualClientInformationService individualClientInformationServiceMock;
    private LegalEntityClientInformationService legalEntityClientInformationServiceMock;
    private UserService userServiceMock;

    private ArgumentCaptor<Contract> contractCaptor = ArgumentCaptor.forClass(Contract.class);
    private ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    @BeforeClass
    public void init() {
        contractService = spy(ContractServiceImpl.class);
        contractRepositoryMock = mock(ContractRepository.class);
        individualClientInformationServiceMock = mock(IndividualClientInformationService.class);
        legalEntityClientInformationServiceMock = mock(LegalEntityClientInformationService.class);
        userServiceMock = mock(UserService.class);
        contractService.setContractRepository(contractRepositoryMock);
        contractService.setIndividualClientInformationService(individualClientInformationServiceMock);
        contractService.setLegalEntityClientInformationService(legalEntityClientInformationServiceMock);
        contractService.setUserService(userServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(contractService, contractRepositoryMock, individualClientInformationServiceMock, legalEntityClientInformationServiceMock, userServiceMock);
    }


    private User createTestAdmin() {
        User user = new User();
        user.setUserId(1L);
        user.setUserRole(UserRole.ADMIN);
        return user;
    }

    private User createTestSeller() {
        User user = new User();
        user.setUserId(1L);
        user.setUserRole(UserRole.SELLER);
        return user;
    }

    private User createTestCustomer() {
        User user = new User();
        user.setUserId(1L);
        user.setUserRole(UserRole.CUSTOMER);
        return user;
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void createShouldThrowExceptionWhenClientAlreadyHasNotDissolvedContract() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(true);

        Contract contract = new Contract();
        contractService.create(contract, "password");
        verify(contractRepositoryMock).existNotDissolvedByClientInformation(contract);
    }

    @Test
    public void createShouldSaveOrUpdateIndividualClientInformation() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(individualClientInformationServiceMock.save(any(IndividualClientInformation.class))).thenReturn(10L);

        Contract contract = createTestContractForIndividual(null);
        contractService.create(contract, "password");
        verify(individualClientInformationServiceMock).save(contract.getIndividualClientInformation());
    }

    @Test
    public void createShouldSaveOrUpdateLegalEntityClientInformation() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(legalEntityClientInformationServiceMock.save(any(LegalEntityClientInformation.class))).thenReturn(10L);

        Contract contract = createTestContractForLegalEntity(null);
        contractService.create(contract, "password");
        verify(legalEntityClientInformationServiceMock).save(contract.getLegalEntityClientInformation());
    }

    @Test
    public void createShouldSaveContract() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);

        Contract contract = createTestContractForIndividual(null);
        contractService.create(contract, "password");
        verify(contractRepositoryMock).create(contract);
    }

    @Test
    public void createShouldSetIndividualClientInformationIdToContract() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(individualClientInformationServiceMock.save(any(IndividualClientInformation.class))).thenReturn(10L);

        contractService.create(createTestContractForIndividual(null), "password");

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        verify(contractRepositoryMock).create(captor.capture());
        assertEquals(captor.getValue().getIndividualClientInformation().getIndividualClientInformationId().longValue(), 10L);
    }

    @Test
    public void createShouldSetLegalEntityClientInformationIdToContract() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(legalEntityClientInformationServiceMock.save(any(LegalEntityClientInformation.class))).thenReturn(10L);

        contractService.create(createTestContractForLegalEntity(null), "password");

        verify(contractRepositoryMock).create(contractCaptor.capture());
        assertEquals(contractCaptor.getValue().getLegalEntityClientInformation().getLegalEntityClientInformationId().longValue(), 10L);
    }

    @Test
    public void createShouldCreateNewUserWithCorrectLogin() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(contractRepositoryMock.create(any(Contract.class))).thenReturn(5L);

        contractService.create(createTestContractForIndividual(null), "password");
        verify(userServiceMock).create(userCaptor.capture());
        assertEquals(userCaptor.getValue().getLogin(), "000005");
    }

    @Test
    public void createShouldCreateNewUserWithCorrectPassword() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(contractRepositoryMock.create(any(Contract.class))).thenReturn(5L);

        contractService.create(createTestContractForIndividual(null), "password");
        verify(userServiceMock).create(userCaptor.capture());
        assertEquals(userCaptor.getValue().getPassword(), "password");
    }

    @Test
    public void createShouldCreateNewUserWithCorrectContractId() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(contractRepositoryMock.create(any(Contract.class))).thenReturn(5L);

        contractService.create(createTestContractForIndividual(null), "password");
        verify(userServiceMock).create(userCaptor.capture());
        assertEquals(userCaptor.getValue().getContract().getContractId().longValue(), 5L);
    }

    @Test
    public void createShouldReturnGeneratedId() throws Exception {
        when(contractRepositoryMock.existNotDissolvedByClientInformation(any(Contract.class))).thenReturn(false);
        when(contractRepositoryMock.create(any(Contract.class))).thenReturn(5L);

        long generatedId = contractService.create(createTestContractForIndividual(null), "password");
        assertEquals(generatedId, 5L);
    }

    @Test
    public void getByIdShouldReturnNullWhenContractDoesNotExists() throws Exception {
        when(contractRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());

        Contract contract = contractService.getById(5L);
        assertNull(contract);
    }

    @Test
    public void getByIdShouldReturnContractWhenContractExists() throws Exception {
        Contract contract = createTestContractForIndividual(15L);
        when(contractRepositoryMock.getById(anyLong())).thenReturn(Optional.of(contract));

        Contract actualContract = contractService.getById(5L);
        assertSame(actualContract, contract);
        verify(contractRepositoryMock).getById(5L);
    }

    @Test
    public void getByIdShouldSetIndividualClientInformationWhenContractClientTypeIsIndividual() throws Exception {
        Contract contract = createTestContractForIndividual(15L);
        IndividualClientInformation individualClientInformation = mock(IndividualClientInformation.class);
        when(contractRepositoryMock.getById(anyLong())).thenReturn(Optional.of(contract));
        when(individualClientInformationServiceMock.getById(anyLong())).thenReturn(individualClientInformation);

        Contract actualContract = contractService.getById(5L);
        assertSame(actualContract.getIndividualClientInformation(), individualClientInformation);
        verify(individualClientInformationServiceMock).getById(15L);
    }

    @Test
    public void getByIdShouldSetLegalEntityClientInformationWhenContractClientTypeIsLegal() throws Exception {
        Contract contract = createTestContractForLegalEntity(15L);
        LegalEntityClientInformation legalEntityClientInformation = mock(LegalEntityClientInformation.class);
        when(contractRepositoryMock.getById(anyLong())).thenReturn(Optional.of(contract));
        when(legalEntityClientInformationServiceMock.getById(anyLong())).thenReturn(legalEntityClientInformation);

        Contract actualContract = contractService.getById(5L);
        assertSame(actualContract.getLegalEntityClientInformation(), legalEntityClientInformation);
        verify(legalEntityClientInformationServiceMock).getById(15L);
    }

    @Test(expectedExceptions = AccessDeniedException.class)
    public void getByIdShouldThrowExceptionIfUserHasRoleAdmin() throws Exception {
        contractService.getByIdOrThrow(5L, createTestAdmin());
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionIfUserIsNotOwner() throws Exception {
        when(contractRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(false);
        contractService.getByIdOrThrow(5L, createTestCustomer());
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionWhenAccountDoesNotExist() throws Exception {
        when(contractRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());
        when(contractRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(true);

        contractService.getByIdOrThrow(5L, createTestCustomer());
    }

    @Test
    public void getByIdOrThrowShouldReturnContractWhenContractExists() throws Exception {
        Contract contract = new Contract();
        doReturn(contract).when(contractService).getById(anyLong());

        Contract actualContract = contractService.getByIdOrThrow(5L, createTestSeller());
        assertSame(actualContract, contract);
        verify(contractService).getById(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdOrThrowShouldThrowExceptionWhenContractDOesNotExist() throws Exception {
        doReturn(null).when(contractService).getById(anyLong());
        when(contractRepositoryMock.isUserOwner(anyLong(), anyLong())).thenReturn(true);
        contractService.getByIdOrThrow(5L, createTestSeller());
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void dissolveShouldThrowExceptionWhenContractDoesNotExist() throws Exception {
        when(contractRepositoryMock.existWithId(anyLong())).thenReturn(false);
        contractService.dissolve(5L);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void dissolveShouldThrowExceptionWhenContractIsAlreadyDissolved() throws Exception {
        when(contractRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(contractRepositoryMock.isDissolved(anyLong())).thenReturn(true);
        contractService.dissolve(5L);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void dissolveShouldThrowExceptionWhenContractHasNotCanceledAnnexes() throws Exception {
        when(contractRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(contractRepositoryMock.isDissolved(anyLong())).thenReturn(false);
        when(contractRepositoryMock.hasNotCanceledContractAnnex(anyLong())).thenReturn(true);
        contractService.dissolve(5L);
    }

    @Test
    public void dissolveShouldUpdateContractStatus() throws Exception {
        when(contractRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(contractRepositoryMock.isDissolved(anyLong())).thenReturn(false);
        when(contractRepositoryMock.hasNotCanceledContractAnnex(anyLong())).thenReturn(false);

        contractService.dissolve(5L);
        verify(contractRepositoryMock).existWithId(5L);
        verify(contractRepositoryMock).isDissolved(5L);
        verify(contractRepositoryMock).hasNotCanceledContractAnnex(5L);
        verify(contractRepositoryMock).dissolve(5L);
    }

    @Test
    public void existByIdShouldReturnTrueWhenContractExists() throws Exception {
        when(contractRepositoryMock.existWithId(anyLong())).thenReturn(true);

        Boolean result = contractService.existById(5L);
        assertTrue(result);
        verify(contractRepositoryMock).existWithId(5L);
    }

    @Test
    public void existByIdShouldReturnFalseWhenContractDoesNotExist() throws Exception {
        when(contractRepositoryMock.existWithId(anyLong())).thenReturn(false);

        Boolean result = contractService.existById(5L);
        assertFalse(result);
        verify(contractRepositoryMock).existWithId(5L);
    }

    private Contract createTestContractForIndividual(Long clientId) {
        Contract contract = new Contract();
        contract.setClientType(ClientType.INDIVIDUAL);
        contract.setIndividualClientInformation(new IndividualClientInformation(clientId));
        return contract;
    }

    private Contract createTestContractForLegalEntity(Long clientId) {
        Contract contract = new Contract();
        contract.setClientType(ClientType.LEGAL);
        contract.setLegalEntityClientInformation(new LegalEntityClientInformation(clientId));
        return contract;
    }
}
