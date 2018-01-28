package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.persistence.entity.LegalEntityClientInformation;
import com.chernik.internetprovider.persistence.repository.LegalEntityClientInformationRepository;
import com.chernik.internetprovider.service.impl.LegalEntityClientInformationServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class LegalEntityClientInformationServiceUnitTest {
    private LegalEntityClientInformationServiceImpl legalEntityClientInformationService;

    private LegalEntityClientInformationRepository legalEntityClientInformationRepositoryMock;

    @BeforeClass
    public void init() {
        legalEntityClientInformationService = new LegalEntityClientInformationServiceImpl();
        legalEntityClientInformationRepositoryMock = mock(LegalEntityClientInformationRepository.class);
        legalEntityClientInformationService.setLegalEntityClientInformationRepository(legalEntityClientInformationRepositoryMock);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(legalEntityClientInformationRepositoryMock);
    }

    @Test
    public void saveShouldCreateClientInformationIfDoesNotExist() throws Exception {
        when(legalEntityClientInformationRepositoryMock.getByPayerAccountNumber(anyString())).thenReturn(Optional.empty());
        when(legalEntityClientInformationRepositoryMock.create(any(LegalEntityClientInformation.class))).thenReturn(5L);

        LegalEntityClientInformation legalEntityClientInformation = createTestLegalEntityClientInformation();
        Long generatedId = legalEntityClientInformationService.save(legalEntityClientInformation);

        assertEquals(generatedId.longValue(), 5L);
        verify(legalEntityClientInformationRepositoryMock).getByPayerAccountNumber("payer account");
        verify(legalEntityClientInformationRepositoryMock).create(legalEntityClientInformation);
    }

    @Test
    public void saveShouldUpdateClientInformationIfExists() throws Exception {
        when(legalEntityClientInformationRepositoryMock.getByPayerAccountNumber(anyString())).thenReturn(Optional.of(createTestLegalEntityClientInformation()));

        LegalEntityClientInformation legalEntityClientInformation = createTestLegalEntityClientInformation();
        Long clientInformationId = legalEntityClientInformationService.save(legalEntityClientInformation);

        assertEquals(clientInformationId.longValue(), 5L);
        verify(legalEntityClientInformationRepositoryMock).getByPayerAccountNumber("payer account");
        verify(legalEntityClientInformationRepositoryMock).update(legalEntityClientInformation);
    }

    @Test
    public void getByPayerAccountNumberShouldReturnClientInformationWhenExists() throws Exception {
        LegalEntityClientInformation clientInformation = createTestLegalEntityClientInformation();
        when(legalEntityClientInformationRepositoryMock.getByPayerAccountNumber(anyString())).thenReturn(Optional.of(clientInformation));

        LegalEntityClientInformation actualClientInformation = legalEntityClientInformationService.getByPayerAccountNumber("payer account");
        assertSame(actualClientInformation, clientInformation);
        verify(legalEntityClientInformationRepositoryMock).getByPayerAccountNumber("payer account");
    }

    @Test
    public void getByPayerAccountNumberShouldReturnNullWhenDoesNotExist() throws Exception {
        when(legalEntityClientInformationRepositoryMock.getByPayerAccountNumber(anyString())).thenReturn(Optional.empty());

        LegalEntityClientInformation actualClientInformation = legalEntityClientInformationService.getByPayerAccountNumber("payer account");
        assertNull(actualClientInformation);
        verify(legalEntityClientInformationRepositoryMock).getByPayerAccountNumber("payer account");
    }

    @Test
    public void getByIdShouldReturnClientInformationWhenExists() throws Exception {
        LegalEntityClientInformation clientInformation = createTestLegalEntityClientInformation();
        when(legalEntityClientInformationRepositoryMock.getById(anyLong())).thenReturn(Optional.of(clientInformation));

        LegalEntityClientInformation actualClientInformation = legalEntityClientInformationService.getById(5L);
        assertSame(actualClientInformation, clientInformation);
        verify(legalEntityClientInformationRepositoryMock).getById(5L);
    }

    @Test
    public void getByIdShouldReturnNullWhenDoesNotExist() throws Exception {
        when(legalEntityClientInformationRepositoryMock.getById(anyLong())).thenReturn(Optional.empty());

        LegalEntityClientInformation actualClientInformation = legalEntityClientInformationService.getById(5L);
        assertNull(actualClientInformation);
        verify(legalEntityClientInformationRepositoryMock).getById(5L);
    }

    private LegalEntityClientInformation createTestLegalEntityClientInformation() {
        LegalEntityClientInformation clientInformation = new LegalEntityClientInformation();
        clientInformation.setLegalEntityClientInformationId(5L);
        clientInformation.setPayerAccountNumber("payer account");
        return clientInformation;
    }
}
