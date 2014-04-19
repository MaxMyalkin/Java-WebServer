package database;

import junit.framework.Assert;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import frontend.Constants;
import resources.ResourceFactory;
/*
 * Created by maxim on 06.03.14.
 */

public class AccountServiceTest {
    private AccountService accountService;
    private String login;
    private String password;
    private boolean isAdded;


    @Before
    public void setUp() throws Exception {
        accountService = new AccountService(new TestDBService(), new MessageSystem(new AddressService()));
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(6);
        isAdded = accountService.addUser(login , password);
    }

    @After
    public void tearDown() throws Exception {
        accountService.deleteUser(login);
    }

    @Test
    public void testGetUserSuccess() throws Exception {
        UsersDataSet user = accountService.getUser(login, password);
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetUserFail() throws Exception {
        UsersDataSet user = accountService.getUser(password, login);
        Assert.assertNull(user);
    }

    @Test
    public void testCheckUserSuccess() throws Exception {
        boolean isExisted = accountService.checkUser(login, password);
        Assert.assertEquals(true , isExisted);
    }

    @Test
    public void testCheckUserPasswordFail() throws Exception {
        boolean isExisted = accountService.checkUser(login, "11".concat(password));
        Assert.assertEquals(false, isExisted);
    }

    @Test
    public void testCheckUserFail() throws Exception {
        boolean isExisted = accountService.checkUser(password, login);
        Assert.assertEquals(false , isExisted);
    }

    @Test
    public void testAddUserSuccess() throws Exception {
        Assert.assertEquals(true , isAdded);
    }

    @Test
    public void testAddUserFail() throws Exception {
        boolean isReinclusion = !accountService.addUser(login, password);
        Assert.assertEquals(true , isReinclusion);
    }

    @Test
    public void testCheckLoginSuccess() throws Exception {
        boolean isExisted = accountService.checkLogin(login);
        Assert.assertEquals(true , isExisted);
    }

    @Test
    public void testCheckLoginFail() throws Exception {
        boolean isExisted = accountService.checkLogin(login.concat("11"));
        Assert.assertEquals(false , isExisted);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        boolean isDeleted = accountService.deleteUser(login);
        Assert.assertEquals(true , isDeleted);
    }

    @Test
    public void testDeleteFail() throws Exception {
        accountService.deleteUser(login);//удаление юзера, добавленного в before
        boolean isDeleted = accountService.deleteUser(login);
        Assert.assertEquals(false , isDeleted);
    }
}
