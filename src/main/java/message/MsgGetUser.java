package message;

import database.AccountService;
import database.UsersDataSet;
import frontend.Constants;
import messageSystem.Address;
import org.hibernate.service.UnknownServiceException;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgGetUser extends MsgToAS {

    class FactoryHelper{
        public MsgUpdateUser makeUpdateMsg( Address from, Address to, String sessionId, UsersDataSet usersDataSet, String message ){
            return new MsgUpdateUser(from, to, sessionId, usersDataSet, message);
        }
    }

    private FactoryHelper factoryHelper;
    public MsgGetUser(Address from, Address to, String name, String password, String sessionId) {
        super(from, to, name, password, sessionId);
        this.factoryHelper = new FactoryHelper();
    }

    public MsgGetUser(Address from, Address to, String name, String password, String sessionId, FactoryHelper factoryHelper) {
        super(from, to, name, password, sessionId);
        this.factoryHelper = factoryHelper;
    }

    void exec(AccountService accountService) {
        try {
            UsersDataSet user = accountService.getUser(name, password);
            String message;
            if (user != null)
                message = Constants.Message.AUTH_SUCCESSFUL;
            else
                message = Constants.Message.AUTH_FAILED;
            accountService.getMessageSystem().sendMessage(new MsgUpdateUser(getTo(), getFrom(),
                    this.sessionID, user, message));
        }
        catch (UnknownServiceException e) {
            accountService.getMessageSystem().sendMessage(new MsgUpdateUser(getTo(), getFrom(),
                    this.sessionID, null, Constants.Message.DATABASE_ERROR));
        }
    }
}
