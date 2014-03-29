package message;

import database.AccountService;
import frontend.Constants;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgRegistrate extends MsgToAS {
    private String sessionID;
    public MsgRegistrate(Address from, Address to, String name, String password , String sessionID) {
        super(from, to , name , password);
        this.sessionID = sessionID;
    }

    void exec(AccountService accountService) {
        if(accountService.addUser(name, password))
            accountService.getMessageSystem().sendMessage(new MsgUpdateRegisterStatus(getTo(), getFrom(),
                    this.sessionID , Constants.Message.SUCCESSFUL_REGISTRATION));
        else
            accountService.getMessageSystem().sendMessage(new MsgUpdateRegisterStatus(getTo(), getFrom(),
                    this.sessionID , Constants.Message.USER_EXISTS ));
    }
}
