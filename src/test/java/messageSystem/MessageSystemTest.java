package messageSystem;

import frontend.Frontend;
import junit.framework.Assert;
import message.Msg;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * Created by maxim on 31.03.14.
 */
public class MessageSystemTest {
    class MsgForTest extends Msg {

        public boolean wasExecuted;

        public MsgForTest(Address from, Address to) {
            super(from, to);
            wasExecuted = false;
        }

        public void exec(Abonent abonent){
            wasExecuted = true;
        }

    }
    private MsgForTest msg;
    private MessageSystem messageSystem = new MessageSystem(new AddressService());
    private Frontend frontend = mock(Frontend.class);

    @Before
    public void setUp() {
        Address from = new Address();
        when(frontend.getAddress()).thenReturn(new Address());
        msg = new MsgForTest(from,frontend.getAddress());
        messageSystem.addAbonent(Frontend.class, frontend);

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
        Assert.assertTrue(msg.wasExecuted);
    }
}
