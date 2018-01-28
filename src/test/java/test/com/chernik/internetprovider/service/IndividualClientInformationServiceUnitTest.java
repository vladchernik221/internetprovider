package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.persistence.entity.IndividualClientInformation;
import com.chernik.internetprovider.persistence.repository.IndividualClientInformationRepository;
import com.chernik.internetprovider.service.impl.IndividualClientInformationServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

public class IndividualClientInformationServiceUnitTest {
    private IndividualClientInformationServiceImpl individualClientInformationService;

    private IndividualClientInformationRepository individualClientInformationRepositoryMock;

    @BeforeClass
    public void init() {
        individualClientInformationService = new IndividualClientInformationServiceImpl();
        individualClientInformationRepositoryMock = mock(IndividualClientInformationRepository.class);
        individualClientInformationService.setIndividualClientInformationRepository(individualClientInformationRepositoryMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(individualClientInformationRepositoryMock);
    }

    @Test
    public void saveShouldCreateClientInformationIfDoesNotExist() throws Exception {
        when(individualClientInformationRepositoryMock.getByPassportData(anyString())).thenReturn(Optional.empty());
        when(individualClientInformationRepositoryMock.create(any(IndividualClientInformation.class))).thenReturn(5L);

        IndividualClientInformation individualClientInformation = createTestIndividualClientInformation();
        Long generatedId = individualClientInformationService.save(individualClientInformation);

        assertEquals(generatedId.longValue(), 5L);
        verify(individualClientInformationRepositoryMock).getByPassportData("passport data");
        verify(individualClientInformationRepositoryMock).create(individualClientInformation);
    }

    @Test
    public void saveShouldUpdateClientInformationIfExists() throws Exception {
        when(individualClientInformationRepositoryMock.getByPassportData(anyString())).thenReturn(Optional.of(createTestIndividualClientInformation()));

        IndividualClientInformation individualClientInformation = createTestIndividualClientInformation();
        Long clientInformationId = individualClientInformationService.save(individualClientInformation);

        assertEquals(clientInformationId.longValue(), 5L);
        verify(individualClientInformationRepositoryMock).getByPassportData("passport data");
        verify(individualClientInformationRepositoryMock).update(individualClientInformation);
    }

    @Test
    public void getByPassportDataShouldReturnClientInformationWhenExists() throws Exception {
        IndividualClientInformation clientInformation = createTestIndividualClientInformation();
        when(individualClientInformationRepositoryMock.getByPassportData(anyString())).thenReturn(Optional.of(clientInformation));

        IndividualClientInformation actualClientInformation = individualClientInformationService.getByPassportData("passport data");
        assertSame(actualClientInformation, clientInformation);
        verify(individualClientInformationRepositoryMock).getByPassportData("passport data");
    }

    @Test
    public void getByPassportDataShouldReturnNullWhenDoesNotExist() throws Exception {
        when(individualClientInformationRepositoryMock.getByPassportData(anyString())).thenReturn(Optional.empty());

        IndividualClientInformation actualClientInformation = individualClientInformationService.getByPassportData("passport data");
        assertNull(actualClientInformation);
        verify(individualClientInformationRepositoryMock).getByPassportData("passport data");
    }

    @Test
    public void getByIdShouldReturnClientInformationWhenExists() throws Exception {
        IndividualClientInformation clientInformation = createTestIndividualClientInformation();
        when(individualClientInformationRepositoryMock.getById(anyLong())).thenReturn(Optional.of(clientInformation));

        IndividualClientInformation actualClientInformation = individualClientInformationService.getById(5L);
        assertSame(actualClientInformation, clientInformation);
        verify(individualClientInformationRepositoryMock).getById(5L);
    }

    @Test
    public void getByIdShouldReturnNullWhenDoesNotExist() throws Exception {
        when(individualClientInformationRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());

        IndividualClientInformation actualClientInformation = individualClientInformationService.getById(5L);
        assertNull(actualClientInformation);
        verify(individualClientInformationRepositoryMock).getById(5L);
    }

    private IndividualClientInformation createTestIndividualClientInformation() {
        IndividualClientInformation clientInformation = new IndividualClientInformation();
        clientInformation.setIndividualClientInformationId(5L);
        clientInformation.setPassportUniqueIdentification("passport data");
        return clientInformation;
    }
}
