package message;

import database.AccountService;
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
    private MessageSystem messageSystem = mock(MessageSystem.class);
    private AddressService addressService = mock(AddressService.class);
    private Frontend frontend = mock(Frontend.class);

    @org.junit.Before
    public void setUp() throws Exception {
        message = Constants.getRandomString(10);
        sessionID = Constants.getRandomString(10);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(addressService.getService(AccountService.class)).thenReturn(new Address());
        when(frontend.getAddress()).thenReturn(new Address());
        msgUpdateRegisterStatus = new MsgUpdateRegisterStatus(frontend.getAddress(),
                addressService.getService(AccountService.class), sessionID, message);
    }

    @org.junit.Test
    public void testExec() throws Exception {
        msgUpdateRegisterStatus.exec(frontend);
        verify(frontend, atLeastOnce()).setMessage(sessionID, message);
    }
}
