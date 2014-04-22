package message;

import database.AccountService;
import exception.DBException;
import helpers.CommonHelper;
import messageSystem.Address;
import messageSystem.MessageSystem;
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
    private String login;
    private String password;
    private String sessionID;
    private Address from;
    private Address to;


    @Before
    public void setUp() throws Exception {
        login = CommonHelper.getRandomString(10);
        password = CommonHelper.getRandomString(10);
        sessionID = CommonHelper.getRandomString(10);
        from = new Address();
        to = new Address();
        msgRegistrate = spy(new MsgRegistrate(from, to, login, password, sessionID));
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExecOk() throws Exception {
        when(accountService.addUser(login, password)).thenReturn(true);
        doReturn(msgUpdateRegisterStatus).when(msgRegistrate).makeUpdateMsg(to, from, sessionID, "Пользователь добавлен");
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }

    @Test
    public void testExecFail() throws Exception {
        when(accountService.addUser(login, password)).thenReturn(false);
        doReturn(msgUpdateRegisterStatus).when(msgRegistrate).makeUpdateMsg(to, from, sessionID, "Пользователь уже существует");
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }

    @Test
    public void testExecDBFail() throws Exception {
        when(accountService.addUser(login, password)).thenThrow(new DBException());
        doReturn(msgUpdateRegisterStatus).when(msgRegistrate).makeUpdateMsg(to, from, sessionID, ((Message) ResourceFactory.instance().get("message.xml")).getDatabaseError());
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }

}
