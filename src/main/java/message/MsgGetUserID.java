package message;

import database.AccountService;
import database.UsersDataSet;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgGetUserID extends MsgToAS {
    private String sessionID;

    public MsgGetUserID(Address from, Address to, String name, String password, String sessionId) {
        super(from, to, name, password);
        this.sessionID = sessionId;
    }

    void exec(AccountService accountService) {
        UsersDataSet user = accountService.getUser(name, password);
        accountService.getMessageSystem().sendMessage(new MsgUpdateUserID(getTo(), getFrom(), this.sessionID, user));
    }
}
