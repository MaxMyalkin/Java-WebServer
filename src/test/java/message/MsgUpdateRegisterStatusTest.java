package message;

import frontend.Constants;
import frontend.Frontend;
import messageSystem.Address;
import messageSystem.AddressService;
import messageSystem.MessageSystem;

import static org.mockito.Mockito.*;

/*
 * Created by maxim on 03.04.14.
 */

public class MsgUpdateRegisterStatusTest {
    private MsgUpdateRegisterStatus msgUpdateRegisterStatus;
    private String message;
    private String sessionID;
    private static MessageSystem MESSAGE_SYSTEM = mock(MessageSystem.class);
    private static AddressService ADDRESS_SERVICE = mock(AddressService.class);
    private static Frontend FRONTEND = mock(Frontend.class);

    @org.junit.Before
    public void setUp() throws Exception {
        message = Constants.getRandomString(10);
        sessionID = Constants.getRandomString(10);
        when(MESSAGE_SYSTEM.getAddressService()).thenReturn(ADDRESS_SERVICE);
        when(ADDRESS_SERVICE.getAccountService()).thenReturn(new Address());
        when(FRONTEND.getAddress()).thenReturn(new Address());
        msgUpdateRegisterStatus = new MsgUpdateRegisterStatus(FRONTEND.getAddress(),
                ADDRESS_SERVICE.getAccountService(), sessionID, message);
    }

    @org.junit.Test
    public void testExec() throws Exception {
        msgUpdateRegisterStatus.exec(FRONTEND);
        verify(FRONTEND, atLeastOnce()).setMessage(sessionID, message);
    }
}
