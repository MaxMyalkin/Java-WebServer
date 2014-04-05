package message;

import database.AccountService;
import frontend.Constants;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.hibernate.service.UnknownServiceException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/*
 * Created by maxim on 03.04.14.
 */
public class MsgRegistrateTest {

    private static AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static MessageSystem MESSAGE_SYSTEM = mock(MessageSystem.class);
    private static MsgUpdateRegisterStatus MSG_UPDATE_REGISTER_STATUS = mock(MsgUpdateRegisterStatus.class);
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
        when(ACCOUNT_SERVICE.getMessageSystem()).thenReturn(MESSAGE_SYSTEM);
    }

    @Test
    public void testExecOk() throws Exception {
        when(ACCOUNT_SERVICE.addUser(login, password)).thenReturn(true);
        doReturn(MSG_UPDATE_REGISTER_STATUS).when(msgRegistrate).makeUpdateMsg(from, to, login, password);
        msgRegistrate.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_REGISTER_STATUS);
    }

    @Test
    public void testExecFail() throws Exception {
        when(ACCOUNT_SERVICE.addUser(login, password)).thenReturn(false);
        doReturn(MSG_UPDATE_REGISTER_STATUS).when(msgRegistrate).makeUpdateMsg(from, to, login, password);
        msgRegistrate.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_REGISTER_STATUS);
    }

    @Test
    public void testExecDBFail() throws Exception {
        when(ACCOUNT_SERVICE.addUser(login, password)).thenThrow(new UnknownServiceException(AccountService.class));
        doReturn(MSG_UPDATE_REGISTER_STATUS).when(msgRegistrate).makeUpdateMsg(to, from, sessionID, Constants.Message.DATABASE_ERROR);
        msgRegistrate.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_REGISTER_STATUS);
    }

}
