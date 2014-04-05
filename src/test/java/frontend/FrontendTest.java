package frontend;

import database.AccountService;
import junit.framework.Assert;
import message.MsgGetUser;
import message.MsgRegistrate;
import messageSystem.Address;
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
 * Created by maxim on 07.03.14.
 */
public class FrontendTest {
    private Frontend frontend;
    private StringWriter stringWriter;
    private String login;
    private String password;
    private String sessionID;

    final static private HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final static private HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final static private HttpSession SESSION = mock(HttpSession.class);
    static private MessageSystem messageSystem;
    final static private AddressService ADDRESS_SERVICE = mock(AddressService.class);
    final static private MsgGetUser MSG_GET_USER = mock(MsgGetUser.class);
    final static private MsgRegistrate MSG_REGISTRATE = mock(MsgRegistrate.class);
    final static private FactoryHelper FACTORY_HELPER = mock(FactoryHelper.class);

    @Before
    public void setUp() throws Exception {
        messageSystem = mock(MessageSystem.class);
        frontend = new Frontend(messageSystem, FACTORY_HELPER);
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(10);
        sessionID = Constants.getRandomString(20);
        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getSession()).thenReturn(SESSION);
        when(REQUEST.getParameter("login")).thenReturn(login);
        when(REQUEST.getParameter("password")).thenReturn(password);
        when(SESSION.getId()).thenReturn(sessionID);
        when(messageSystem.getAddressService()).thenReturn(ADDRESS_SERVICE);
        when(ADDRESS_SERVICE.getService(AccountService.class)).thenReturn(new Address());
    }


    @Test
    public void testDoGetIndexPage() throws Exception {
        String url = Constants.Url.INDEX;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Индекс"));
    }

    @Test
    public void testDoGetAuthPage() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Форма авторизации"));
    }

    @Test
    public void testDoGetRegisterPage() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.addUserSession(login, sessionID).setMessage("some message");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("some message"));
    }

    @Test
    public void testDoGetSessionPageWithoutAuth() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("Авторизуйтесь на /authform"));
    }

    @Test
    public void testDoGetSessionPageAuthSuccess() throws Exception {
        String url = Constants.Url.SESSION;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.addUserSession(login, sessionID).setMessage(Constants.Message.AUTH_SUCCESSFUL);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(login));
    }

    @Test
    public void testDoGetRedirectToIndexFromAnotherURL() throws Exception {
        String url = "/somethingelse";
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doGet(REQUEST, RESPONSE);
        verify(RESPONSE , atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testDoPostAuthWithoutParameters() throws Exception {
        when(REQUEST.getParameter("login")).thenReturn("");
        when(REQUEST.getParameter("password")).thenReturn("");
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(FACTORY_HELPER.makeMsgGetUser(frontend.getAddress(), ADDRESS_SERVICE.getService(AccountService.class),
                login, password, sessionID)).thenReturn(MSG_GET_USER);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.SESSION);
        verify(messageSystem, never()).sendMessage(MSG_GET_USER);
    }

    @Test
    public void testDoPostAuthWithParameters() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(FACTORY_HELPER.makeMsgGetUser(frontend.getAddress(), ADDRESS_SERVICE.getService(AccountService.class), login, password, sessionID)).thenReturn(MSG_GET_USER);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.SESSION);
        verify(messageSystem, atLeastOnce()).sendMessage(MSG_GET_USER);
    }

    @Test
    public void testDoPostRegistrationWithoutParameters() throws Exception {
        when(REQUEST.getParameter("login")).thenReturn("");
        when(REQUEST.getParameter("password")).thenReturn("");
        when(FACTORY_HELPER.makeMsgRegistrate(frontend.getAddress(), ADDRESS_SERVICE.getService(AccountService.class),
                login, password, sessionID)).thenReturn(MSG_REGISTRATE);
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
        verify(messageSystem, never()).sendMessage(MSG_REGISTRATE);
    }

    @Test
    public void testDoPostRegistrationWithParameters() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(REQUEST.getPathInfo()).thenReturn(url);
        when(FACTORY_HELPER.makeMsgRegistrate(frontend.getAddress(), ADDRESS_SERVICE.getService(AccountService.class), login, password, sessionID)).thenReturn(MSG_REGISTRATE);
        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
        verify(messageSystem, atLeastOnce()).sendMessage(MSG_REGISTRATE);
    }

    @Test
    public void testLogout() throws Exception {
        String url = Constants.Url.LOGOUT;
        when(REQUEST.getPathInfo()).thenReturn(url);
        frontend.addUserSession(login,sessionID);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertNull(frontend.getUserSession(sessionID));
        verify(RESPONSE, atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }
}
