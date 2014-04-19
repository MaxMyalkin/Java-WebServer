package frontend;

import junit.framework.Assert;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
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
    final private HttpServletRequest request = mock(HttpServletRequest.class);
    final private HttpServletResponse response = mock(HttpServletResponse.class);
    final private HttpSession session = mock(HttpSession.class);
    StringWriter stringWriter;

    @Before
    public void setUp() throws Exception
    {
        frontend = new Frontend(new MessageSystem(new AddressService()));
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
    }


    public void testDoGet(String url , String answer) throws Exception
    {
        when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains(answer));
    }

    @Test
    public void testAllRoutes() throws Exception
    {
        String[][] urls = new String[][]{
                { "/index", "Индекс" },
                { "/userid", "Сессия" },
                { "/authform", "Форма авторизации" },
                { "/registerform", "Форма регистрации" },
                { "/else", "Индекс" }
        };

        for (String[] i : urls)
        {
            testDoGet(i[0], i[1]);
        }
    }

}
