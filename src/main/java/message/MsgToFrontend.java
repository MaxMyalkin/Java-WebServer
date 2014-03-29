package message;

import frontend.Frontend;
import messageSystem.Abonent;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public abstract class MsgToFrontend extends Msg{

    public MsgToFrontend(Address from, Address to){
        super(from,to);
    }

    public void exec(Abonent frontend) {
        if(frontend instanceof Frontend){
            exec((Frontend)frontend);
        }
    }

    abstract void exec(Frontend frontend);
}
