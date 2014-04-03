package frontend;

import message.MsgGetUser;
import message.MsgRegistrate;
import messageSystem.Address;

/*
 * Created by maxim on 03.04.14.
 */
class FactoryHelper {
    public MsgGetUser makeMsgGetUser(Address from, Address to, String login, String password, String sessionID) {
       return new MsgGetUser(from, to, login, password, sessionID);
    }

    public MsgRegistrate makeMsgRegistrate(Address from, Address to, String login, String password, String sessionID) {
        return new MsgRegistrate(from, to, login, password, sessionID);
    }
}
