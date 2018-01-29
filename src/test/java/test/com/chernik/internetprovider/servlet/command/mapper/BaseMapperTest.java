package test.com.chernik.internetprovider.servlet.command.mapper;

import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.service.RegularExpressionService;
import com.chernik.internetprovider.service.impl.RegularExpressionServiceImpl;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class BaseMapperTest {
    private BaseMapper baseMapper;
    private HttpServletRequest requestMock;

    @BeforeClass
    public void init() {
        RegularExpressionService regularExpressionService = new RegularExpressionServiceImpl();
        requestMock = mock(HttpServletRequest.class);

        baseMapper = new BaseMapper();
        baseMapper.setRegularExpressionService(regularExpressionService);
    }

    @BeforeMethod
    public void resetMock() {
        reset(requestMock);
    }

    @Test
    public void getMandatoryStringShouldReturnStringWhenParameterExist() throws Exception {
        String parameterName = "testParameter";
        String expected = "test";

        when(requestMock.getParameter(parameterName)).thenReturn(expected);

        String actual = baseMapper.getMandatoryString(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryStringShouldThrowBadRequestExceptionWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expected = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expected);

        try {
            baseMapper.getMandatoryString(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryStringShouldThrowBadRequestExceptionWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);

        try {
            baseMapper.getMandatoryString(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test
    public void getNotMandatoryStringShouldReturnStringWhenParameterExist() {
        String parameterName = "testParameter";
        String expected = "test";

        when(requestMock.getParameter(parameterName)).thenReturn(expected);

        String actual = baseMapper.getNotMandatoryString(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryStringShouldReturnNullWhenParameterIsEmpty() {
        String parameterName = "testParameter";
        String expected = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expected);

        String actual = baseMapper.getNotMandatoryString(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryStringShouldReturnNullWhenParameterDoesNotExist() {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);


        String actual = baseMapper.getNotMandatoryString(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }


    @Test
    public void getMandatoryIntShouldReturnIntegerWhenParameterExistAndHaveRightConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "123";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Integer expected = Integer.valueOf(expectedString);
        Integer actual = baseMapper.getMandatoryInt(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryIntShouldThrowBadRequestExceptionWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryInt(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryIntShouldThrowBadRequestExceptionWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryInt(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryIntShouldThrowBadRequestExceptionWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);

        try {
            baseMapper.getMandatoryString(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test
    public void getNotMandatoryIntShouldReturnIntegerWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "123";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Integer expected = Integer.valueOf(expectedString);
        Integer actual = baseMapper.getNotMandatoryInt(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryIntShouldReturnNullWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Integer actual = baseMapper.getNotMandatoryInt(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryIntShouldReturnNullWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);


        Integer actual = baseMapper.getNotMandatoryInt(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }


    @Test
    public void getMandatoryBigDecimalShouldReturnBigDecimalWhenParameterExistAndHaveRightConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "123.12";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        BigDecimal expected = new BigDecimal(expectedString);
        BigDecimal actual = baseMapper.getMandatoryBigDecimal(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryBigDecimalShouldThrowBadRequestExceptionWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryBigDecimal(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryBigDecimalShouldThrowBadRequestExceptionWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryInt(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryBigDecimalShouldThrowBadRequestExceptionWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);

        try {
            baseMapper.getMandatoryBigDecimal(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test
    public void getNotMandatoryBigDecimalShouldReturnBigDecimalWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "123.0";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        BigDecimal expected = new BigDecimal(expectedString);
        BigDecimal actual = baseMapper.getNotMandatoryBigDecimal(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryBigDecimalShouldReturnNullWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        BigDecimal actual = baseMapper.getNotMandatoryBigDecimal(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryBigDecimalShouldReturnNullWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);


        BigDecimal actual = baseMapper.getNotMandatoryBigDecimal(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }


    @Test
    public void getMandatoryLongShouldReturnLongWhenParameterExistAndHaveRightConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "123";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Long expected = Long.valueOf(expectedString);
        Long actual = baseMapper.getMandatoryLong(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryLongShouldThrowBadRequestExceptionWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryLong(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryLongShouldThrowBadRequestExceptionWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryLong(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryLongShouldThrowBadRequestExceptionWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);

        try {
            baseMapper.getMandatoryLong(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test
    public void getNotMandatoryLongShouldReturnLongWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "123";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Long expected = Long.valueOf(expectedString);
        Long actual = baseMapper.getNotMandatoryLong(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryLongShouldReturnNullWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Long actual = baseMapper.getNotMandatoryLong(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryLongShouldReturnNullWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);


        Long actual = baseMapper.getNotMandatoryLong(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }


    @Test
    public void getMandatoryDateShouldReturnDateWhenParameterExistAndHaveRightConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "2018-01-28";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Date expected = Date.valueOf(expectedString);
        Date actual = baseMapper.getMandatoryDate(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryDateShouldThrowBadRequestExceptionWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryDate(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryDateShouldThrowBadRequestExceptionWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        try {
            baseMapper.getMandatoryDate(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void getMandatoryDateShouldThrowBadRequestExceptionWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);

        try {
            baseMapper.getMandatoryDate(requestMock, parameterName);
        } catch (Exception e) {
            verify(requestMock).getParameter(parameterName);
            throw e;
        }
    }

    @Test
    public void getNotMandatoryBooleanShouldReturnIntegerWhenParameterExistAndHaveBadConsistence() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "true";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Boolean expected = Boolean.valueOf(expectedString);
        Boolean actual = baseMapper.getNotMandatoryBoolean(requestMock, parameterName);

        assertEquals(actual, expected);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryBooleanShouldReturnNullWhenParameterIsEmpty() throws Exception {
        String parameterName = "testParameter";
        String expectedString = "";

        when(requestMock.getParameter(parameterName)).thenReturn(expectedString);

        Boolean actual = baseMapper.getNotMandatoryBoolean(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }

    @Test
    public void getNotMandatoryBooleanShouldReturnNullWhenParameterDoesNotExist() throws Exception {
        String parameterName = "testParameter";

        when(requestMock.getParameter(parameterName)).thenReturn(null);


        Boolean actual = baseMapper.getNotMandatoryBoolean(requestMock, parameterName);

        assertNull(actual);
        verify(requestMock).getParameter(parameterName);
    }
}