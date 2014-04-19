package message;

import database.AccountService;
import exception.DBException;
import messageSystem.Address;
import resources.Message;
import resources.ResourceFactory;

/*
 * Created by maxim on 29.03.14.
 */

public class MsgRegistrate extends MsgToAS {

    public MsgRegistrate(Address from, Address to, String name, String password , String sessionID) {
        super(from, to , name , password, sessionID);
    }

    public MsgUpdateRegisterStatus makeUpdateMsg( Address from, Address to, String sessionId, String message ){
        return new MsgUpdateRegisterStatus(from, to, sessionId, message);
    }

    void exec(AccountService accountService) {
        try {
            String message;
            if(accountService.addUser(name, password))
                message = ((Message) ResourceFactory.instance().get("message.xml")).getSuccessfulRegistration();
            else
                message = ((Message) ResourceFactory.instance().get("message.xml")).getUserExists();
            accountService.getMessageSystem().sendMessage(makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID , message));
        }
        catch (DBException ex){
            accountService.getMessageSystem().sendMessage(makeUpdateMsg(getTo(), getFrom(),
                    this.sessionID , ((Message) ResourceFactory.instance().get("message.xml")).getDatabaseError() ));
        }
    }
}
