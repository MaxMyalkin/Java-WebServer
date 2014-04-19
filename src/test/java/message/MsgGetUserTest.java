package message;

import database.AccountService;
import database.UsersDataSet;
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

public class MsgGetUserTest {

    private final AccountService accountService = mock(AccountService.class);
    private final MessageSystem messageSystem = mock(MessageSystem.class);
    private final MsgUpdateUser msgUpdateUser = mock(MsgUpdateUser.class);
    private MsgGetUser msgGetUser;
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
        msgGetUser = spy(new MsgGetUser(from, to, login, password , sessionID));
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExecFail() throws Exception {
        when(accountService.getUser(login, password)).thenReturn(null);
        doReturn(msgUpdateUser).when(msgGetUser).makeUpdateMsg(from, to, login, null, sessionID);
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }

    @Test
    public void testExecOk() throws Exception {
        UsersDataSet usersDataSet = new UsersDataSet(login,password);
        when(accountService.getUser(login, password)).thenReturn(usersDataSet);
        doReturn(msgUpdateUser).when(msgGetUser).makeUpdateMsg(from, to, login, usersDataSet, sessionID);
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }

    @Test
    public void testExecDBFail() throws Exception {
        when(accountService.getUser(login, password)).thenThrow(new UnknownServiceException(AccountService.class));
        doReturn(msgUpdateUser).when(msgGetUser).makeUpdateMsg(to, from, sessionID, null,
                "База данных недоступна");
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }

}
