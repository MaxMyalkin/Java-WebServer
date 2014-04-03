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

    private static AccountService accountService = mock(AccountService.class);
    private static MessageSystem messageSystem = mock(MessageSystem.class);
    private static MsgRegistrate.FactoryHelper factoryHelper = mock(MsgRegistrate.FactoryHelper.class);
    private static MsgUpdateRegisterStatus msgUpdateRegisterStatus = mock(MsgUpdateRegisterStatus.class);
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
        msgRegistrate = new MsgRegistrate(from, to, login, password, sessionID, factoryHelper);
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        when(accountService.addUser(login, password)).thenReturn(true);
        when(factoryHelper.makeUpdateMsg(to, from, sessionID, Constants.Message.SUCCESSFUL_REGISTRATION))
                .thenReturn(msgUpdateRegisterStatus);
        msgRegistrate.exec(accountService);
        verify(messageSystem, atLeastOnce()).sendMessage(msgUpdateRegisterStatus);
    }
}
