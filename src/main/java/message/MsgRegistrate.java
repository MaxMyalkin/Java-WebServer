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
        String info = "";
        Message message = (Message) ResourceFactory.instance().get("message.xml");
        try {
            if(accountService.addUser(name, password))
                info = message.getSuccessfulRegistration();
            else
                info = message.getUserExists();
        }
        catch (DBException ex){
            info = message.getDatabaseError();
        }
        finally {
            accountService.getMessageSystem().sendMessage(makeUpdateMsg(getTo(), getFrom(), this.sessionID , info));
        }
    }
}
