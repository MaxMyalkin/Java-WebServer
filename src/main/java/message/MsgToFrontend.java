package message;

import frontend.Frontend;
import messageSystem.Abonent;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public abstract class MsgToFrontend extends Msg{

    protected String message;
    protected String sessionID;

    public MsgToFrontend(Address from, Address to , String sessionID, String message){
        super(from,to);
        this.sessionID = sessionID;
        this.message = message;
    }

    public void exec(Abonent frontend) {
        if(frontend instanceof Frontend){
            exec((Frontend)frontend);
        }
    }

    abstract void exec(Frontend frontend);
}
