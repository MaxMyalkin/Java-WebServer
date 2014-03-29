package message;

import database.AccountService;
import database.UsersDataSet;
import frontend.Constants;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgGetUser extends MsgToAS {
    private String sessionID;

    public MsgGetUser(Address from, Address to, String name, String password, String sessionId) {
        super(from, to, name, password);
        this.sessionID = sessionId;
    }

    void exec(AccountService accountService) {
        UsersDataSet user = accountService.getUser(name, password);
        String message;
        if (user != null)
            message = Constants.Message.AUTH_SUCCESSFUL;
        else
            message = Constants.Message.AUTH_FAILED;
        accountService.getMessageSystem().sendMessage(new MsgUpdateUser(getTo(), getFrom(), this.sessionID, user , message));
    }
}
