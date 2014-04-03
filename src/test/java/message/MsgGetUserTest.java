package message;

import database.AccountService;
import database.UsersDataSet;
import frontend.Constants;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/*
 * Created by maxim on 03.04.14.
 */

public class MsgGetUserTest {

    private final static AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private final static MessageSystem MESSAGE_SYSTEM = mock(MessageSystem.class);
    private final static MsgGetUser.FactoryHelper FACTORY_HELPER = mock(MsgGetUser.FactoryHelper.class);
    private final static MsgUpdateUser MSG_UPDATE_USER = mock(MsgUpdateUser.class);
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
        msgGetUser = new MsgGetUser(from, to, login, password , sessionID, FACTORY_HELPER);
        when(ACCOUNT_SERVICE.getMessageSystem()).thenReturn(MESSAGE_SYSTEM);
    }

    @Test
    public void testExecFail() throws Exception {
        when(ACCOUNT_SERVICE.getUser(login, password)).thenReturn(null);
        when(FACTORY_HELPER.makeUpdateMsg(to, from, sessionID,null,
                Constants.Message.AUTH_FAILED)).thenReturn(MSG_UPDATE_USER);
        msgGetUser.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_USER);
    }

    @Test
    public void testExecOk() throws Exception {
        UsersDataSet usersDataSet = new UsersDataSet(login,password);
        when(ACCOUNT_SERVICE.getUser(login, password)).thenReturn(usersDataSet);
        when(FACTORY_HELPER.makeUpdateMsg(to, from, sessionID,usersDataSet,
                Constants.Message.AUTH_SUCCESSFUL)).thenReturn(MSG_UPDATE_USER);
        msgGetUser.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_USER);
    }

}
