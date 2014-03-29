package message;

import database.UsersDataSet;
import frontend.Frontend;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgUpdateUser extends MsgToFrontend {
    private String sessionID;
    private UsersDataSet user;
    private String message;

    public MsgUpdateUser(Address from, Address to, String sessionID, UsersDataSet user , String message){
        super(from,to);
        this.sessionID = sessionID;
        this.user = user;
        this.message = message;
    }

    public void exec(Frontend frontend){
        frontend.setMessage(sessionID,message);
        try{
            frontend.setUser(sessionID, user);
        }
        catch (NullPointerException e){
            frontend.setUser(sessionID, new UsersDataSet("",""));
        }
    }
}
