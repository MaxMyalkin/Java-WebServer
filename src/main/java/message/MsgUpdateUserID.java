package message;

import database.UsersDataSet;
import frontend.Frontend;
import messageSystem.Address;

/*
 * Created by maxim on 29.03.14.
 */
public class MsgUpdateUserID extends MsgToFrontend {
    private String sessionID;
    private UsersDataSet user;

    public MsgUpdateUserID(Address from, Address to, String sessionID, UsersDataSet user){
        super(from,to);
        this.sessionID = sessionID;
        this.user = user;
    }

    public void exec(Frontend frontend){
        frontend.setUserID(sessionID,user.getId());
        frontend.setUserName(sessionID,user.getLogin());
    }
}
