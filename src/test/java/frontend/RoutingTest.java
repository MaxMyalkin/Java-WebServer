package frontend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/*
 * Created by maxim on 11.03.14.
 */
public class RoutingTest {
    private static String url;
    private Frontend frontend;
    final static private HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final static private HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final static private HttpSession SESSION = mock(HttpSession.class);

    @Before
    public void setUp() throws Exception
    {
        frontend = new Frontend();
        StringWriter stringWriter;
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getSession()).thenReturn(SESSION);
        when(SESSION.getAttribute("userId")).thenReturn(new Long(100));
    }

    @Test
    public void testDoGet() throws Exception
    {
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , never()).sendRedirect(Constants.Url.INDEX);
    }

    public static Boolean performRoutingTest(String _url)
    {
        JUnitCore core = new JUnitCore();
        url = _url;
        return core.run(RoutingTest.class).wasSuccessful();

    }

}
