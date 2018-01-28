package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.exception.EntityNotFoundException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.persistence.repository.ContractAnnexRepository;
import com.chernik.internetprovider.service.ContractService;
import com.chernik.internetprovider.service.TariffPlanService;
import com.chernik.internetprovider.service.impl.ContractAnnexServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ContractAnnexServiceUnitTest {
    private ContractAnnexServiceImpl contractAnnexService;

    private ContractAnnexRepository contractAnnexRepositoryMock;
    private TariffPlanService tariffPlanServiceMock;
    private ContractService contractServiceMock;

    @BeforeClass
    public void init() {
        contractAnnexService = new ContractAnnexServiceImpl();
        contractAnnexRepositoryMock = mock(ContractAnnexRepository.class);
        tariffPlanServiceMock = mock(TariffPlanService.class);
        contractServiceMock = mock(ContractService.class);
        contractAnnexService.setContractAnnexRepository(contractAnnexRepositoryMock);
        contractAnnexService.setTariffPlanService(tariffPlanServiceMock);
        contractAnnexService.setContractService(contractServiceMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(contractAnnexRepositoryMock, tariffPlanServiceMock, contractServiceMock);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void createShouldThrowExceptionWhenTariffPlanDoesNotExist() throws Exception {
        when(tariffPlanServiceMock.existWithId(anyLong())).thenReturn(false);

        contractAnnexService.create(createTestContractAnnex());
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void createShouldThrowExceptionWhenContractDoesNotExist() throws Exception {
        when(tariffPlanServiceMock.existWithId(anyLong())).thenReturn(true);
        when(contractServiceMock.getByIdOrThrow(anyLong())).thenThrow(EntityNotFoundException.class);

        contractAnnexService.create(createTestContractAnnex());
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void createShouldThrowExceptionWhenContractIsDissolved() throws Exception {
        Contract contract = new Contract();
        contract.setDissolved(true);
        when(tariffPlanServiceMock.existWithId(anyLong())).thenReturn(true);
        when(contractServiceMock.getByIdOrThrow(anyLong())).thenReturn(contract);

        contractAnnexService.create(createTestContractAnnex());
    }

    @Test
    public void createShouldSaveContractAnnex() throws Exception {
        Contract contract = new Contract();
        contract.setDissolved(false);
        when(tariffPlanServiceMock.existWithId(anyLong())).thenReturn(true);
        when(contractServiceMock.getByIdOrThrow(anyLong())).thenReturn(contract);

        ContractAnnex contractAnnex = createTestContractAnnex();
        contractAnnexService.create(contractAnnex);

        verify(tariffPlanServiceMock).existWithId(5L);
        verify(contractServiceMock).getByIdOrThrow(15L);
        verify(contractAnnexRepositoryMock).create(contractAnnex);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getPageShouldThrowExceptionWhenContractDoesNotExist() throws Exception {
        when(contractServiceMock.existById(anyLong())).thenReturn(false);

        contractAnnexService.getPage(25L, new Pageable(3, 10));
    }

    @Test
    public void getPageShouldGetContractAnnexesFromRepository() throws Exception {
        when(contractServiceMock.existById(anyLong())).thenReturn(true);
        Page<ContractAnnex> contractAnnexPage = new Page<>(createTestContractAnnexList(), 4);
        when(contractAnnexRepositoryMock.getPage(anyLong(), any(Pageable.class))).thenReturn(contractAnnexPage);

        Pageable pageable = new Pageable(3, 10);
        Page<ContractAnnex> actualContractAnnexPage = contractAnnexService.getPage(25L, pageable);

        assertSame(actualContractAnnexPage, contractAnnexPage);
        verify(contractAnnexRepositoryMock, times(1)).getPage(25L, pageable);
    }

    @Test
    public void getPageShouldSetTariffPlanForEachContractAnnex() throws Exception {
        when(contractServiceMock.existById(anyLong())).thenReturn(true);
        when(contractAnnexRepositoryMock.getPage(anyLong(), any(Pageable.class))).thenReturn(new Page<>(createTestContractAnnexList(), 4));

        contractAnnexService.getPage(25L, new Pageable(3, 10));

        verify(tariffPlanServiceMock).getById(1L);
        verify(tariffPlanServiceMock).getById(2L);
        verify(tariffPlanServiceMock).getById(3L);
    }

    @Test
    public void getPageShouldReturnContractAnnexesWithTariffPlans() throws Exception {
        when(contractServiceMock.existById(anyLong())).thenReturn(true);
        when(contractAnnexRepositoryMock.getPage(anyLong(), any(Pageable.class))).thenReturn(new Page<>(createTestContractAnnexList(), 4));
        List<TariffPlan> tariffPlanList = Arrays.asList(mock(TariffPlan.class), mock(TariffPlan.class), mock(TariffPlan.class));
        when(tariffPlanServiceMock.getById(anyLong())).thenReturn(tariffPlanList.get(0)).thenReturn(tariffPlanList.get(1)).thenReturn(tariffPlanList.get(2));

        Page<ContractAnnex> contractAnnexPage = contractAnnexService.getPage(25L, new Pageable(3, 10));
        assertEquals(contractAnnexPage.getData().stream().map(ContractAnnex::getTariffPlan).collect(Collectors.toList()), tariffPlanList);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void getByIdShouldThrowExceptionWhenContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());

        contractAnnexService.getById(25L);
    }

    @Test
    public void getByIdShouldReturnContractAnnexWhenContractAnnexExists() throws Exception {
        ContractAnnex contractAnnex = createTestContractAnnex();
        when(contractAnnexRepositoryMock.getById(anyLong())).thenReturn(Optional.of(contractAnnex));

        ContractAnnex actualContractAnnex = contractAnnexService.getById(25L);
        assertSame(actualContractAnnex, contractAnnex);
        verify(contractAnnexRepositoryMock).getById(25L);
    }

    @Test
    public void getByIdShouldSetTariffPlanToContractAnnexWhenContractAnnexExists() throws Exception {
        ContractAnnex contractAnnex = createTestContractAnnex();
        TariffPlan tariffPlan = mock(TariffPlan.class);
        when(contractAnnexRepositoryMock.getById(anyLong())).thenReturn(Optional.of(contractAnnex));
        when(tariffPlanServiceMock.getById(anyLong())).thenReturn(tariffPlan);

        ContractAnnex actualContractAnnex = contractAnnexService.getById(25L);
        assertSame(actualContractAnnex.getTariffPlan(), tariffPlan);
        verify(tariffPlanServiceMock).getById(5L);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void cancelShouldThrowExceptionWhenContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexRepositoryMock.existWithId(anyLong())).thenReturn(false);

        contractAnnexService.cancel(5L);
    }

    @Test(expectedExceptions = UnableSaveEntityException.class)
    public void cancelShouldThrowExceptionWhenContractAnnexIsAlreadyCanceled() throws Exception {
        when(contractAnnexRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(contractAnnexRepositoryMock.isCanceled(anyLong())).thenReturn(true);

        contractAnnexService.cancel(5L);
    }

    @Test
    public void cancelShouldUpdateCanceledStatus() throws Exception {
        when(contractAnnexRepositoryMock.existWithId(anyLong())).thenReturn(true);
        when(contractAnnexRepositoryMock.isCanceled(anyLong())).thenReturn(false);

        contractAnnexService.cancel(5L);
        verify(contractAnnexRepositoryMock).cancel(5L);
    }

    @Test
    public void existByIdShouldReturnTrueIfContractAnnexExists() throws Exception {
        when(contractAnnexRepositoryMock.existWithId(anyLong())).thenReturn(true);

        Boolean actualResult = contractAnnexService.existById(5L);
        assertTrue(actualResult);
        verify(contractAnnexRepositoryMock).existWithId(5L);
    }

    @Test
    public void existByIdShouldReturnFalseIfContractAnnexDoesNotExist() throws Exception {
        when(contractAnnexRepositoryMock.existWithId(anyLong())).thenReturn(false);

        Boolean actualResult = contractAnnexService.existById(5L);
        assertFalse(actualResult);
        verify(contractAnnexRepositoryMock).existWithId(5L);
    }

    private ContractAnnex createTestContractAnnex() {
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setTariffPlan(new TariffPlan(5L));
        contractAnnex.setContract(new Contract(15L));
        return contractAnnex;
    }

    private List<ContractAnnex> createTestContractAnnexList() {
        List<ContractAnnex> contractAnnexList = new ArrayList<>();
        ContractAnnex contractAnnex;
        for(long id = 1L; id < 4L; id++) {
            contractAnnex = new ContractAnnex();
            contractAnnex.setTariffPlan(new TariffPlan(id));
            contractAnnexList.add(contractAnnex);
        }
        return contractAnnexList;
    }
}
