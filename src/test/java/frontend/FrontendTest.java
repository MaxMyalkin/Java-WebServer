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
    private AccountService accountService;
    private StringWriter stringWriter;

    private String login;
    private String password;
    final static private HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final static private HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final static private HttpSession SESSION = mock(HttpSession.class);

    @Before
    public void setUp() throws Exception {
        frontend = new Frontend();
        accountService = new AccountService();
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
    public void testGetIndexPage() throws Exception {
        String url = Constants.Url.INDEX;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Индекс"));
    }

    @Test
    public void testGetAuthPageWithNoErrors() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertFalse(stringWriter.toString().contains(Constants.Message.AUTH_FAILED));
    }

    @Test
    public void testGetAuthPageWithError() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("wrong");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.AUTH_FAILED));
    }

    @Test
    public void testGetRegisterPageWithNoErrors() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Форма регистрации"));
    }

    @Test
    public void testGetRegisterPageWithEmptyError() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("error");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.EMPTY_FIELDS));
    }

    @Test
    public void testGetRegisterPageWithUserExistError() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("exist");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.USER_EXISTS));
    }

    @Test
    public void testGetRegisterPageWithUserAddedInfo() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("info")).thenReturn("ok");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(Constants.Message.SUCCESSFUL_REGISTRATION));
    }

    @Test
    public void testGetSessionPage() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn(new Long(100));
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("100"));
    }

    @Test
    public void testRedirectToIndexWithoutUserID() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn(null);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testRedirectToIndexWithoutSession() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getSession()).thenReturn(null);
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
        url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        accountService.addUser(login,password);
        frontend.doPost(REQUEST, RESPONSE);
        accountService.deleteUser(login);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testRedirectToIndexFromAnotherURL() throws Exception {
        String url = "/somethingelse";
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testAuthWithWrongParameters() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "wrong");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.AUTHFORM);
    }

    @Test
    public void testAuthSuccessful() throws Exception {

        String url = Constants.Url.AUTHFORM;
        accountService.addUser(login, password);
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(SESSION.getAttribute("userId")).thenReturn(new Long(100));
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, times(1)).sendRedirect(Constants.Url.SESSION);
        accountService.deleteUser(login);
    }

    @Test
    public void testRegistrationWithoutParameters() throws Exception {
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
    public void testRegistrationrWithExistedUser() throws Exception {
        accountService.addUser(login , password);
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "exist");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
        accountService.deleteUser(login);
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doPost(REQUEST, RESPONSE);
        verify(SESSION , atLeastOnce()).setAttribute("info", "ok");
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
        accountService.deleteUser(login);
    }

}
