package frontend;

import database.AccountService;
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
 * Created by maxim on 07.03.14.
 */

public class FrontendTest {
    private Frontend frontend;
    private StringWriter stringWriter;

    private String login;
    private String password;
    final static private HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final static private HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final static private HttpSession SESSION = mock(HttpSession.class);
    final static private AccountService ACCOUNT_SERVICE = mock(AccountService.class);


    @Before
    public void setUp() throws Exception {
        frontend = new Frontend(ACCOUNT_SERVICE);
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(10);
        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getSession()).thenReturn(SESSION);
        when(REQUEST.getParameter("login")).thenReturn(login);
        when(REQUEST.getParameter("password")).thenReturn(password);
    }


    @Test
    public void testDoGetIndexPage() throws Exception {
        String url = Constants.Url.INDEX;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Индекс"));
    }

    @Test
    public void testDoGetAuthPageWithNoErrors() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertFalse(stringWriter.toString().contains(Constants.Message.AUTH_FAILED));
    }

    @Test
    public void testDoGetAuthPageWithError() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("wrong");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.AUTH_FAILED));
    }

    @Test
    public void testDoGetRegisterPageWithoutErrors() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Форма регистрации"));
    }

    @Test
    public void testDoGetRegisterPageWithEmptyError() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("error");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.EMPTY_FIELDS));
    }

    @Test
    public void testDoGetRegisterPageWithUserExistError() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("exist");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.USER_EXISTS));
    }

    @Test
    public void testDoGetRegisterPageWithUserAddedInfo() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("ok");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.SUCCESSFUL_REGISTRATION));
    }

    @Test
    public void testDoGetSessionPage() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn((long)100);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("100"));
    }

    @Test
    public void testDoGetRedirectToIndexWithoutUserID() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testDoGetRedirectToIndexWithoutSession() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getSession()).thenReturn(null);
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }


    @Test
    public void testDoGetRedirectToIndexFromAnotherURL() throws Exception {
        String url = "/somethingelse";
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testDoPostRedirectToIndexWithoutSession() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getSession()).thenReturn(null);
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(ACCOUNT_SERVICE.checkUser(login , password)).thenReturn(true);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testDoPostAuthWithWrongParameters() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(ACCOUNT_SERVICE.checkUser(password, login)).thenReturn(false);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "wrong");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.AUTHFORM);
    }

    @Test
    public void testDoPostAuthSuccessful() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn((long)100);
        when(ACCOUNT_SERVICE.checkUser(login , password)).thenReturn(true);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.SESSION);
    }

    @Test
    public void testDoPostRegistrationWithoutParameters() throws Exception {
        login = "";
        password = "";
        when(REQUEST.getParameter("login")).thenReturn(login);
        when(REQUEST.getParameter("password")).thenReturn(password);
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "error");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
    }

    @Test
    public void testDoPostRegistrationWithExistedUser() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(ACCOUNT_SERVICE.addUser(login , password)).thenReturn(false);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "exist");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
    }

    @Test
    public void testDoPostSuccessfulRegistration() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(ACCOUNT_SERVICE.addUser(login , password)).thenReturn(true);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "ok");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
    }

}
