package database;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by maxim on 07.03.14.
 */
public class UsersDataSetTest {
    private UsersDataSet usersDataSet;
    int id;
    String login;
    String password;
    @Before
    public void setUp() throws Exception {
        usersDataSet = new UsersDataSet();
        id = 100;
        login = "login";
        password = "password";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSetId() throws Exception {
        usersDataSet.setId(id);
        Assert.assertEquals(usersDataSet.getId() , id);
    }

    @Test
    public void testSetLogin() throws Exception {
        usersDataSet.setLogin(login);
        Assert.assertEquals(usersDataSet.getLogin() , login);
    }

    @Test
    public void testSetPassword() throws Exception {
        usersDataSet.setPassword(password);
        Assert.assertEquals(usersDataSet.getPassword() , password);
    }

    @Test
    public void testGetId() throws Exception {
        usersDataSet.setId(id);
        Assert.assertEquals(usersDataSet.getId() , id);
    }

    @Test
    public void testGetLogin() throws Exception {
        usersDataSet.setLogin(login);
        Assert.assertEquals(usersDataSet.getLogin() , login);

    }

    @Test
    public void testGetPassword() throws Exception {
        usersDataSet.setPassword(password);
        Assert.assertEquals(usersDataSet.getPassword() , password);

    }
}
