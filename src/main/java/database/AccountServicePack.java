package database;

import frontend.Constants;
import messageSystem.MessageSystem;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by maxim on 05.04.14.
 */
public class AccountServicePack {

    private final List<AccountService> accountServiceList = new ArrayList<>();

    public AccountServicePack(Integer port, MessageSystem messageSystem, int numberOfElements) {
        for( int i = 0 ; i < numberOfElements ; i++) {
            if(port == Constants.MAIN_PORT) {
                accountServiceList.add(new AccountService(new DBService(), messageSystem));
            }
            else {
                accountServiceList.add(new AccountService(new TestDBService(), messageSystem));
            }
        }
    }

    public void startAccountServices() {
        for(AccountService accountService : accountServiceList)
        {
            new Thread(accountService).start();
        }
    }

}
