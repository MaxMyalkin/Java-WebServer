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

    private static AccountService accountService = mock(AccountService.class);
    private static MessageSystem messageSystem = mock(MessageSystem.class);
    private static MsgGetUser.FactoryHelper factoryHelper = mock(MsgGetUser.FactoryHelper.class);
    private static MsgUpdateUser msgUpdateUser = mock(MsgUpdateUser.class);
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
        msgGetUser = new MsgGetUser(from, to, login, password , Constants.getRandomString(10), factoryHelper);
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        when(accountService.getUser(login, password)).thenReturn(null);
        when(factoryHelper.makeUpdateMsg(to, from, sessionID,new UsersDataSet(login,password),
                Constants.Message.DATABASE_ERROR)).thenReturn(msgUpdateUser);
        msgGetUser.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateUser);
    }
}
