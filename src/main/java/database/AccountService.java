package database;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxim on 22.02.14.
 */
public class AccountService {
    private Map<String,String> auth;
    public AccountService()
    {
        auth = new HashMap<>();
    }
    public void addUser(String login , String password)
    {
        if(!checkUser(login,password))
            auth.put(login,password);
    }

    public boolean checkUser(String login , String password)
    {
        return (auth.containsKey(login)&& password.equals(auth.get(login))) ? true : false;
    }

}
