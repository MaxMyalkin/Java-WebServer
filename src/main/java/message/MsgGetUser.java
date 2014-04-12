package message;

import database.AccountService;
import database.UsersDataSet;
import exception.DBException;
import frontend.Constants;
import messageSystem.Address;
import org.hibernate.service.UnknownServiceException;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgGetUser extends MsgToAS {

    public MsgGetUser(Address from, Address to, String name, String password, String sessionId) {
        super(from, to, name, password, sessionId);
    }

    public MsgUpdateUser makeUpdateMsg( Address from, Address to, String sessionId, UsersDataSet usersDataSet, String message ) {
        return new MsgUpdateUser(from, to, sessionId, usersDataSet, message);
    }

    void exec(AccountService accountService) {
        try {
            UsersDataSet user = accountService.getUser(name, password);
            String message;
            if (user != null)
                message = Constants.Message.AUTH_SUCCESSFUL;
            else
                message = Constants.Message.AUTH_FAILED;
            accountService.getMessageSystem().sendMessage(makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID, user, message));
        }
        catch (DBException e) {
            accountService.getMessageSystem().sendMessage(makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID, null, Constants.Message.DATABASE_ERROR));
        }
    }
}
