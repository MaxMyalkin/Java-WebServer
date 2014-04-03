package message;

import frontend.Frontend;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgUpdateRegisterStatus extends MsgToFrontend {

    public MsgUpdateRegisterStatus(Address from, Address to,String sessionID, String status){
        super(from,to, sessionID, status);
    }

    public void exec(Frontend frontend){
        frontend.setMessage(this.sessionID , this.message);
    }
}
