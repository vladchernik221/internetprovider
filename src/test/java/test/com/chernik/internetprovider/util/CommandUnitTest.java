package test.com.chernik.internetprovider.util;

import com.chernik.internetprovider.context.ContextInitializer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CommandUnitTest {
    protected HttpServletRequest requestMock;
    protected HttpServletResponse responseMock;
    protected HttpSession sessionMock;

    public void init() {
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
    }

    public void resetMocks() throws Exception {
        reset(requestMock, responseMock, sessionMock);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(mock(RequestDispatcher.class));
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    protected <T> T getMapper(Class<T> clazz) {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        return contextInitializer.getComponent(clazz);
    }
}
