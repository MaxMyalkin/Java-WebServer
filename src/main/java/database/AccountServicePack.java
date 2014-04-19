package database;

import resources.DBSettings;
import messageSystem.MessageSystem;
import resources.ResourceFactory;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by maxim on 05.04.14.
 */
public class AccountServicePack {

    private final List<AccountService> accountServiceList = new ArrayList<>();

    public AccountServicePack(Integer port, MessageSystem messageSystem, int numberOfElements) {
        for( int i = 0 ; i < numberOfElements ; i++) {
            if(port == ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getPort()) {
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
