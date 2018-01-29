package test.com.chernik.internetprovider.servlet.command;

import com.chernik.internetprovider.servlet.command.impl.ChangeLocaleCommandPost;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.CommandUnitTest;

import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.mockito.Mockito.*;

public class ChangeLocaleCommandPostUnitTest extends CommandUnitTest {
    private ChangeLocaleCommandPost command;

    private HttpSession sessionMock;

    @BeforeClass
    public void init() {
        super.init();
        command = new ChangeLocaleCommandPost();
        command.setBaseMapper(getMapper(BaseMapper.class));

        sessionMock = mock(HttpSession.class);
    }

    @BeforeMethod
    public void resetMocks() throws Exception {
        super.resetMocks();
        when(requestMock.getParameter("localeLanguage")).thenReturn("en");
        when(requestMock.getParameter("localeRegion")).thenReturn("US");
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    public void executeShouldSetLocaleToSession() throws Exception {
        command.execute(requestMock, responseMock);
        verify(sessionMock).setAttribute("locale", new Locale("en", "US"));
    }
}
