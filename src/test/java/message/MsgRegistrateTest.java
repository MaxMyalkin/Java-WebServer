package message;

import database.AccountService;
import frontend.Constants;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.hibernate.service.UnknownServiceException;
import org.junit.Before;
import org.junit.Test;
import resources.Message;
import resources.ResourceFactory;

import static org.mockito.Mockito.*;

/*
 * Created by maxim on 03.04.14.
 */
public class MsgRegistrateTest {

    private AccountService accountService = mock(AccountService.class);
    private MessageSystem messageSystem = mock(MessageSystem.class);
    private MsgUpdateRegisterStatus msgUpdateRegisterStatus = mock(MsgUpdateRegisterStatus.class);
    private MsgRegistrate msgRegistrate;
    String login;
    String password;
    String sessionID;
    Address from;
    Address to;


    @Before
    public void setUp() throws Exception {
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(10);
        sessionID = Constants.getRandomString(10);
        from = new Address();
        to = new Address();
        msgRegistrate = spy(new MsgRegistrate(from, to, login, password, sessionID));
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExecOk() throws Exception {
        when(accountService.addUser(login, password)).thenReturn(true);
        doReturn(msgUpdateRegisterStatus).when(msgRegistrate).makeUpdateMsg(from, to, login, password);
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }

    @Test
    public void testExecFail() throws Exception {
        when(accountService.addUser(login, password)).thenReturn(false);
        doReturn(msgUpdateRegisterStatus).when(msgRegistrate).makeUpdateMsg(from, to, login, password);
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }

    @Test
    public void testExecDBFail() throws Exception {
        when(accountService.addUser(login, password)).thenThrow(new UnknownServiceException(AccountService.class));
        doReturn(msgUpdateRegisterStatus).when(msgRegistrate).makeUpdateMsg(to, from, sessionID, ((Message) ResourceFactory.instance().get("message.xml")).getDatabaseError());
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }

}
