package test.com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.servlet.command.RegexHandler;
import com.chernik.internetprovider.servlet.command.RequestParameter;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class RegexHandlerUnitTest {

    private RegexHandler regexHandler;
    private RequestParameter testParameter;

    @BeforeClass
    public void init() {
        regexHandler = new RegexHandler();
        testParameter = new RequestParameter("/test/{\\d+}/123", RequestType.POST);
        regexHandler.put(testParameter);
    }

    @Test
    public void getShouldReturnRequestParameterWhenHandlerHaveRequestParameter() {
        RequestParameter parameter = new RequestParameter("/test/43/123", RequestType.POST);
        RequestParameter expected = testParameter;
        RequestParameter actual = regexHandler.get(parameter);

        assertEquals(actual, expected);
    }

    @Test
    public void getShouldReturnNullWhenHandlerHaveNotRequestParameterWithUri(){
        RequestParameter parameter = new RequestParameter("/test/wrong/123", RequestType.POST);
        RequestParameter actual = regexHandler.get(parameter);

        assertNull(actual);
    }

    @Test
    public void getShouldReturnNullWhenHandlerHaveNotRequestParameterWithMethod(){
        RequestParameter parameter = new RequestParameter("/test/43/123", RequestType.GET);
        RequestParameter actual = regexHandler.get(parameter);

        assertNull(actual);
    }
}
