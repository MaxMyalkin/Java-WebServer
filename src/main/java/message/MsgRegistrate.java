package message;

import database.AccountService;
import frontend.Constants;
import messageSystem.Address;
import org.hibernate.service.UnknownServiceException;

/*
 * Created by maxim on 29.03.14.
 */

public class MsgRegistrate extends MsgToAS {

    class FactoryHelper{
        public MsgUpdateRegisterStatus makeUpdateMsg( Address from, Address to, String sessionId, String message ){
            return new MsgUpdateRegisterStatus(from, to, sessionId, message);
        }
    }

    private FactoryHelper factoryHelper;

    public MsgRegistrate(Address from, Address to, String name, String password , String sessionID) {
        super(from, to , name , password, sessionID);
        this.factoryHelper = new FactoryHelper();
    }

    public MsgRegistrate(Address from, Address to, String name, String password , String sessionID, FactoryHelper factoryHelper) {
        super(from, to , name , password, sessionID);
        this.factoryHelper = factoryHelper;
    }

    void exec(AccountService accountService) {
        try {
            String message;
            if(accountService.addUser(name, password))
                message = Constants.Message.SUCCESSFUL_REGISTRATION;
            else
                message = Constants.Message.USER_EXISTS;
            accountService.getMessageSystem().sendMessage(factoryHelper.makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID , message));
        }
        catch (UnknownServiceException ex){
            accountService.getMessageSystem().sendMessage(factoryHelper.makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID , Constants.Message.DATABASE_ERROR ));
        }
    }
}
