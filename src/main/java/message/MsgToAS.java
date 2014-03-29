package message;

import database.AccountService;
import messageSystem.Abonent;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public abstract class MsgToAS extends Msg {

    String name;
    String password;

    public MsgToAS(Address from, Address to , String name , String password){
        super(from,to);
        this.name = name;
        this.password = password;
    }

    public void exec(Abonent accountService) {
        if(accountService instanceof AccountService){
            exec((AccountService)accountService);
        }
    }

    abstract void exec(AccountService accountService);
}
