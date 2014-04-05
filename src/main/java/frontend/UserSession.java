package frontend;

import database.AccountService;
import messageSystem.Address;
import messageSystem.AddressService;

/*
 * Created by maxim on 29.03.14.
 */
public class UserSession {
    private String sessionID;
    private String name;
    private Long userID;
    private Address accountServiceAddress;
    private String message;

    public UserSession(String name , String sessionID , AddressService addressService, String message){
        this.name = name;
        this.sessionID = sessionID;
        this.accountServiceAddress = addressService.getService(AccountService.class);
        this.message = message;
    }

    public String getName(){
        return name;
    }

    public String getSessionID(){
        return sessionID;
    }

    public Long getUserID(){
        return userID;
    }

    public Address getAccountServiceAddress(){
        return accountServiceAddress;
    }

    public void setUserID(Long userID){
        this.userID = userID;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String Message){
        this.message = Message;
    }

    public void setUserName(String name){
        this.name = name;
    }
}
