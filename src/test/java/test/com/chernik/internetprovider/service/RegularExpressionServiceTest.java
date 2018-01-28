package test.com.chernik.internetprovider.service;

import com.chernik.internetprovider.service.RegularExpressionService;
import com.chernik.internetprovider.service.impl.RegularExpressionServiceImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RegularExpressionServiceTest {
    private RegularExpressionService regularExpressionService = new RegularExpressionServiceImpl();

    @Test
    public void checkToShouldReturnTrueWhenTextMatchesToRegularExpression() {
        Boolean result = regularExpressionService.checkTo("123", "\\d+");
        assertTrue(result);
    }

    @Test
    public void checkToShouldReturnFalseWhenTextDoesNotMatchToRegularExpression() {
        Boolean result = regularExpressionService.checkTo("abc", "\\d+");
        assertFalse(result);
    }
}
