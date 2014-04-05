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
    private final static MessageSystem MESSAGE_SYSTEM = mock(MessageSystem.class);
    private final static AddressService ADDRESS_SERVICE = mock(AddressService.class);
    private final static Frontend FRONTEND = mock(Frontend.class);
    private final static UsersDataSet USERS_DATA_SET = mock(UsersDataSet.class);


    @org.junit.Before
    public void setUp() throws Exception {
        message = Constants.getRandomString(10);
        sessionID = Constants.getRandomString(10);
        when(MESSAGE_SYSTEM.getAddressService()).thenReturn(ADDRESS_SERVICE);
        when(ADDRESS_SERVICE.getService(AccountService.class)).thenReturn(new Address());
        when(FRONTEND.getAddress()).thenReturn(new Address());
    }

    @org.junit.Test
    public void testExecOk() throws Exception {
        MsgUpdateUser msgUpdateUser = new MsgUpdateUser(FRONTEND.getAddress(),
                ADDRESS_SERVICE.getService(AccountService.class), sessionID,USERS_DATA_SET, message);
        msgUpdateUser.exec(FRONTEND);
        verify(FRONTEND, atLeastOnce()).setUser(sessionID,USERS_DATA_SET);
    }


}
