package message;

import database.AccountService;
import frontend.Constants;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/*
 * Created by maxim on 03.04.14.
 */
public class MsgRegistrateTest {

    private static AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static MessageSystem MESSAGE_SYSTEM = mock(MessageSystem.class);
    private static MsgRegistrate.FactoryHelper FACTOTY_HELPER = mock(MsgRegistrate.FactoryHelper.class);
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
        msgRegistrate = new MsgRegistrate(from, to, login, password, sessionID, FACTOTY_HELPER);
        when(ACCOUNT_SERVICE.getMessageSystem()).thenReturn(MESSAGE_SYSTEM);
    }

    @Test
    public void testExecOk() throws Exception {
        when(ACCOUNT_SERVICE.addUser(login, password)).thenReturn(true);
        when(FACTOTY_HELPER.makeUpdateMsg(to, from, sessionID, Constants.Message.SUCCESSFUL_REGISTRATION))
                .thenReturn(MSG_UPDATE_REGISTER_STATUS);
        msgRegistrate.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_REGISTER_STATUS);
    }

    @Test
    public void testExecFail() throws Exception {
        when(ACCOUNT_SERVICE.addUser(login, password)).thenReturn(false);
        when(FACTOTY_HELPER.makeUpdateMsg(to, from, sessionID, Constants.Message.USER_EXISTS))
                .thenReturn(MSG_UPDATE_REGISTER_STATUS);
        msgRegistrate.exec(ACCOUNT_SERVICE);
        verify(MESSAGE_SYSTEM, atLeastOnce()).sendMessage(MSG_UPDATE_REGISTER_STATUS);
    }

}
