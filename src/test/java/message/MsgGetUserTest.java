package message;

import database.AccountService;
import database.UsersDataSet;
import exception.DBException;
import helpers.CommonHelper;
import messageSystem.Address;
import messageSystem.MessageSystem;
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
        msgGetUser = spy(new MsgGetUser(from, to, login, password , sessionID));
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExecFail() throws Exception {
        when(accountService.getUser(login, password)).thenReturn(null);
        doReturn(msgUpdateUser).when(msgGetUser).makeUpdateMsg(to, from, sessionID, null, "Неправильные логин/пароль");
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }

    @Test
    public void testExecOk() throws Exception {
        UsersDataSet usersDataSet = new UsersDataSet(login,password);
        when(accountService.getUser(login, password)).thenReturn(usersDataSet);
        doReturn(msgUpdateUser).when(msgGetUser).makeUpdateMsg(to, from, sessionID, usersDataSet, "Вы успешно авторизовались");
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }

    @Test
    public void testExecDBFail() throws Exception {
        when(accountService.getUser(login, password)).thenThrow(new DBException());
        doReturn(msgUpdateUser).when(msgGetUser).makeUpdateMsg(to, from, sessionID, null,
                "База данных недоступна");
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }

}
