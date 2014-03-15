package frontend;

import database.AccountService;
import database.TestDBService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

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
    private Frontend frontend;
    final static private HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final static private HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final static private HttpSession SESSION = mock(HttpSession.class);
    StringWriter stringWriter;

    @Before
    public void setUp() throws Exception
    {
        frontend = new Frontend(new AccountService(new TestDBService()));

        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getSession()).thenReturn(SESSION);
        when(SESSION.getAttribute("userId")).thenReturn((long)100);
    }


    public void testDoGet(String url , String answer) throws Exception
    {
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(answer));
    }

    @Test
    public void testAllRoutes() throws Exception
    {
        String[][] urls = new String[][]{
                { Constants.Url.INDEX, "Индекс" },
                { Constants.Url.SESSION, "Сессия" },
                { Constants.Url.AUTHFORM, "Форма авторизации" },
                { Constants.Url.REGISTERFORM, "Форма регистрации" },
                { "/else", "Индекс" }
        };

        for (String[] i : urls)
        {
            testDoGet(i[0], i[1]);
        }
    }

}
