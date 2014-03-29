package frontend;

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
    private String isRegistrated;

    public UserSession(String name , String sessionID , AddressService addressService, String isRegistrated){
        this.name = name;
        this.sessionID = sessionID;
        this.accountServiceAddress = addressService.getAccountService();
        this.isRegistrated = isRegistrated;
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

    public String getIsRegistrated(){
        return isRegistrated;
    }

    public void setIsRegistrated(String isRegistrated){
        this.isRegistrated = isRegistrated;
    }

    public void setUserName(String name){
        this.name = name;
    }
}
