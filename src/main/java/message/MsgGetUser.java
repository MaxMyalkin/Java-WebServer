package message;

import database.AccountService;
import database.UsersDataSet;
import exception.DBException;
import messageSystem.Address;
import resources.Message;
import resources.ResourceFactory;

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
        Message message = (Message) ResourceFactory.instance().get("message.xml");
        String info = "";
        UsersDataSet user = null;
        try {
            user = accountService.getUser(name, password);
            if (user != null)
                info = message.getAuthSuccessful();
            else
                info = message.getAuthFailed();
        }
        catch (DBException e) {
            info = message.getDatabaseError();
        }
        finally {
            accountService.getMessageSystem().sendMessage(makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID, user, info));
        }
    }
}
