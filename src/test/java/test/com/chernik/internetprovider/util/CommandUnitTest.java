package test.com.chernik.internetprovider.util;

import com.chernik.internetprovider.context.ContextInitializer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CommandUnitTest {
    protected HttpServletRequest requestMock;
    protected HttpServletResponse responseMock;

    public void init() {
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
    }

    public void resetMocks() throws Exception {
        reset(requestMock, responseMock);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(mock(RequestDispatcher.class));
    }

    protected <T> T getMapper(Class<T> clazz) {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        return contextInitializer.getComponent(clazz);
    }
}
