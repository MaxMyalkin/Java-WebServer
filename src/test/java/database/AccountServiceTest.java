package database;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by maxim on 06.03.14.
 */

public class AccountServiceTest {
    private AccountService accountService;
    private String login;
    private String password;
    private String getRandomString( int length) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < length ; ++i )
        {
            string.append((char)(Math.random()*(90 - 65) + 65));
        }
        return string.toString();
    }


    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
        login = getRandomString(10);
        password = getRandomString(6);

    }

    @Test
    public void testCheckUser() throws Exception {
        accountService.addUser(login , password);
        Assert.assertTrue(accountService.checkUser(login , password));
        Assert.assertFalse(accountService.checkUser(password, login));
        Assert.assertFalse(accountService.checkUser(login , new String().concat("11").concat(password)));
        accountService.delete(login);
    }

    @Test
    public void testAddUser() throws Exception {
        Assert.assertTrue(accountService.addUser(login , password));
        Assert.assertTrue(accountService.checkUser(login, password));
        Assert.assertFalse(accountService.addUser(login, password));
        accountService.delete(login);
    }

    @Test
    public void testCheckLogin() throws Exception {
        accountService.addUser(login , password);
        Assert.assertTrue( accountService.checkLogin(login));
        Assert.assertFalse(accountService.checkLogin(new String(login + "11")));
        accountService.delete(login);
    }

    @Test
    public void testDelete() throws Exception {
        accountService.addUser(login , password);
        Assert.assertTrue( accountService.checkLogin(login));
        accountService.delete(login);
        Assert.assertFalse(accountService.checkLogin(login));
    }
}
