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
    private Frontend testFrontend;
    final private StringWriter stringWriter = new StringWriter();
    final private String login = Constants.getRandomString(10);
    final private String password = Constants.getRandomString(10);
    final private String sessionID = Constants.getRandomString(10);
    final private Address accountServiceAddress = new Address();
    final private Address frontendAddress = new Address();


    final private HttpServletRequest request = mock(HttpServletRequest.class);
    final private HttpServletResponse response = mock(HttpServletResponse.class);
    final private HttpSession session = mock(HttpSession.class);
    private MessageSystem messageSystem;
    final private AddressService addressService = mock(AddressService.class);
    final private MsgGetUser msgGetUser = mock(MsgGetUser.class);
    final private MsgRegistrate msgRegistrate = mock(MsgRegistrate.class);



    @Before
    public void setUp() throws Exception {
        messageSystem = mock(MessageSystem.class);
        frontend = new Frontend(messageSystem);
        testFrontend = spy(frontend);

        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);
        when(session.getId()).thenReturn(sessionID);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(addressService.getService(AccountService.class)).thenReturn(accountServiceAddress);
        when(testFrontend.getAddress()).thenReturn(frontendAddress);
    }

    @Test
    public void testDoGetIndexPage() throws Exception {
        String url = Constants.Url.INDEX;
        when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Индекс"));
    }

    @Test
    public void testDoGetAuthPage() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Форма авторизации"));
    }

    @Test
    public void testDoGetRegisterPage() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(request.getPathInfo()).thenReturn(url);
        frontend.addUserSession(login, sessionID).setMessage("some message");
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("some message"));
    }

    @Test
    public void testDoGetSessionPageWithoutAuth() throws Exception {
        String url = Constants.Url.SESSION;
        when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Авторизуйтесь на /authform"));
    }

    @Test
    public void testDoGetSessionPageAuthSuccess() throws Exception {
        String url = Constants.Url.SESSION;
        when(request.getPathInfo()).thenReturn(url);
        frontend.addUserSession(login, sessionID).setMessage(Constants.Message.AUTH_SUCCESSFUL);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains(login));
    }

    @Test
    public void testDoGetRedirectToIndexFromAnotherURL() throws Exception {
        String url = "/somethingelse";
        when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }

    @Test
    public void testDoPostAuthWithoutParameters() throws Exception {
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        String url = Constants.Url.AUTHFORM;
        when(request.getPathInfo()).thenReturn(url);
        when(testFrontend.makeMsgGetUser(frontend.getAddress(), addressService.getService(AccountService.class),
                login, password, sessionID)).thenReturn(msgGetUser);
        testFrontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(Constants.Url.SESSION);
        verify(messageSystem, never()).sendMessage(msgGetUser);
    }

    @Test
    public void testDoPostAuthWithParameters() throws Exception {
        String url = Constants.Url.AUTHFORM;
        when(request.getPathInfo()).thenReturn(url);
        when(testFrontend.makeMsgGetUser(frontend.getAddress(),
                addressService.getService(AccountService.class), login, password, sessionID)).thenReturn(msgGetUser);
        testFrontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(Constants.Url.SESSION);
        verify(messageSystem, atLeastOnce()).sendMessage(msgGetUser);
    }

    @Test
    public void testDoPostRegistrationWithoutParameters() throws Exception {
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(testFrontend.makeMsgRegistrate(frontend.getAddress(), addressService.getService(AccountService.class),
                login, password, sessionID)).thenReturn(msgRegistrate);
        String url = Constants.Url.REGISTERFORM;
        when(request.getPathInfo()).thenReturn(url);
        testFrontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
        verify(messageSystem, never()).sendMessage(msgRegistrate);
    }

    @Test
    public void testDoPostRegistrationWithParameters() throws Exception {
        String url = Constants.Url.REGISTERFORM;
        when(request.getPathInfo()).thenReturn(url);
        when(testFrontend.makeMsgRegistrate(frontend.getAddress(),
                addressService.getService(AccountService.class), login, password, sessionID)).thenReturn(msgRegistrate);
        testFrontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(Constants.Url.REGISTERFORM);
        verify(messageSystem, atLeastOnce()).sendMessage(msgRegistrate);
    }

    @Test
    public void testLogout() throws Exception {
        String url = Constants.Url.LOGOUT;
        when(request.getPathInfo()).thenReturn(url);
        frontend.addUserSession(login,sessionID);
        frontend.doGet(request, response);
        Assert.assertNull(frontend.getUserSession(sessionID));
        verify(response, atLeastOnce()).sendRedirect(Constants.Url.INDEX);
    }
}
