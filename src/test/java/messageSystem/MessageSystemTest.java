package messageSystem;

import frontend.Constants;
import frontend.Frontend;
import junit.framework.Assert;
import message.Msg;
import message.MsgRegistrate;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * Created by maxim on 31.03.14.
 */
public class MessageSystemTest {
    private Msg msg;
    private MessageSystem messageSystem = new MessageSystem(new AddressService());
    private static Frontend frontend = mock(Frontend.class);

    @Before
    public void setUp() {
        Address from = new Address();
        String login = Constants.getRandomString(10);
        String password = Constants.getRandomString(10);
        String sessionID = Constants.getRandomString(10);
        when(frontend.getAddress()).thenReturn(new Address());
        msg = new MsgRegistrate(from,frontend.getAddress(),login,password,sessionID);
        messageSystem.addAbonent(frontend);

    }

    @Test
    public void testSendMessage() throws Exception {
        messageSystem.sendMessage(msg);
        Assert.assertFalse(messageSystem.getQueue(frontend).isEmpty());
    }

    @Test
    public void testExecForAbonent() throws Exception {
        messageSystem.sendMessage(msg);
        messageSystem.execForAbonent(frontend);
        Assert.assertTrue(messageSystem.getQueue(frontend).isEmpty());
    }
}
