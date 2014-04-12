package message;

/*
 * Created by maxim on 03.04.14.
 */

import database.AccountService;
import database.UsersDataSet;
import frontend.Constants;
import frontend.Frontend;
import messageSystem.Address;
import messageSystem.AddressService;
import messageSystem.MessageSystem;

import static org.mockito.Mockito.*;

public class MsgUpdateUserTest {

    private String message;
    private String sessionID;
    private static MessageSystem messageSystem = mock(MessageSystem.class);
    private static AddressService addressService = mock(AddressService.class);
    private static Frontend frontend = mock(Frontend.class);
    private static UsersDataSet usersDataSet = mock(UsersDataSet.class);


    @org.junit.Before
    public void setUp() throws Exception {
        message = Constants.getRandomString(10);
        sessionID = Constants.getRandomString(10);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(addressService.getService(AccountService.class)).thenReturn(new Address());
        when(frontend.getAddress()).thenReturn(new Address());
    }

    @org.junit.Test
    public void testExecOk() throws Exception {
        MsgUpdateUser msgUpdateUser = new MsgUpdateUser(frontend.getAddress(),
                addressService.getService(AccountService.class), sessionID, usersDataSet, message);
        msgUpdateUser.exec(frontend);
        verify(frontend, atLeastOnce()).setUser(sessionID, usersDataSet);
    }


}
