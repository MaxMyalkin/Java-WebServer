package frontend;

import database.AccountService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/**
 * Created by maxim on 07.03.14.
 */
public class FrontendTest {
    private Frontend frontend;
    private AccountService accountService;
    final static private HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final static private HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final static private HttpSession SESSION = mock(HttpSession.class);

    @Before
    public void setUp() throws Exception {
        frontend = new Frontend();
        accountService = new AccountService();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoGet() throws Exception {
        StringWriter stringWrite = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWrite);
        String url = "/authform";

        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getSession()).thenReturn(SESSION);
        when(REQUEST.getPathInfo()).thenReturn(url);

        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWrite.toString().contains("<title>Авторизация</title>"));

        url = "/registerform";
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST,RESPONSE);
        Assert.assertTrue(stringWrite.toString().contains("<title>Регистрация</title>"));

        url = "/userid";
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn(new Long(100));
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE, never()).sendRedirect("/index.html");
        Assert.assertTrue(stringWrite.toString().contains("<p>ID пользователя: 100</p>"));

        when(SESSION.getAttribute("userId")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect("/index.html");

    }

    @Test
    public void testDoPost() throws Exception {
        StringWriter stringWrite = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWrite);
        final String login = "login";
        final String password = "password";
        String url = "/authform";
        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getParameter("password")).thenReturn("password");
        when(REQUEST.getParameter("login")).thenReturn("login");
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(REQUEST.getSession()).thenReturn(SESSION);

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/authform?info=error");

        accountService.addUser(login,password);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect("/userid");
        accountService.delete(login);

        url = "/registerform";
        when(REQUEST.getPathInfo()).thenReturn(url);

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect("registerform?info=ok");

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/registerform?info=exist");

        when(REQUEST.getParameter("login")).thenReturn("");
        when(REQUEST.getParameter("password")).thenReturn("");
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/registerform?info=error");

        accountService.delete(login);

    }
}
