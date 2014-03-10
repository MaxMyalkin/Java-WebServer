package database;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import frontend.Constants;
/*
 * Created by maxim on 06.03.14.
 */

public class AccountServiceTest {
    private AccountService accountService;
    private String login;
    private String password;




    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(6);
    }

    @Test
    public void testCheckUserSuccess() throws Exception {
        accountService.addUser(login , password);
        Assert.assertTrue(accountService.checkUser(login , password));
        accountService.deleteUser(login);
    }

    @Test
    public void testCheckUserPasswordFail() throws Exception {
        accountService.addUser(login , password);
        Assert.assertFalse(accountService.checkUser(login, "11".concat(password)));
        accountService.deleteUser(login);
    }

    @Test
    public void testCheckUserFail() throws Exception {
        accountService.addUser(login , password);
        Assert.assertFalse(accountService.checkUser(password, login));
        accountService.deleteUser(login);
    }

    @Test
    public void testAddUserSuccess() throws Exception {
        Assert.assertTrue(accountService.addUser(login, password));
        accountService.deleteUser(login);
    }

    @Test
    public void testAddUserFail() throws Exception {
        accountService.addUser(login , password);
        Assert.assertFalse(accountService.addUser(login, password));
        accountService.deleteUser(login);
    }

    @Test
    public void testCheckLoginSuccess() throws Exception {
        accountService.addUser(login , password);
        Assert.assertTrue( accountService.checkLogin(login));
        accountService.deleteUser(login);
    }

    @Test
    public void testCheckLoginFail() throws Exception {
        accountService.addUser(login , password);
        Assert.assertFalse(accountService.checkLogin(login.concat("11")));
        accountService.deleteUser(login);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        accountService.addUser(login , password);
        Assert.assertTrue(accountService.deleteUser(login));
    }

    @Test
    public void testDeleteFail() throws Exception {
        Assert.assertFalse(accountService.deleteUser(login));
    }

}
