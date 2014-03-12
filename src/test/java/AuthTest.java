import com.sun.istack.internal.NotNull;
import database.AccountService;
import frontend.Constants;
import junit.framework.Assert;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import server.ServerConfigurator;

import java.util.NoSuchElementException;

/*
 * Created by maxim on 08.03.14.
 */
public class AuthTest {
    private AccountService accountService;
    private String login;
    private String password;
    private Server server;

    public boolean testLogin(@NotNull String url,@NotNull String username,@NotNull String password) {


        WebDriver driver = new HtmlUnitDriver(true);
        driver.get(url);

        WebElement loginEl = driver.findElement(By.name("login"));
                loginEl.sendKeys(username);
        WebElement passwordEl = driver.findElement(By.name("password"));
                passwordEl.sendKeys(password);
        WebElement btnElement = driver.findElement(By.className("btn-submit"));
                btnElement.submit();

        boolean result;
        try
        {
            WebDriverWait wait = new WebDriverWait(driver , 10);
            ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
                @Override
                @NotNull
                public Boolean apply(@NotNull WebDriver d) {
                    WebElement id;
                    try
                    {
                        id = d.findElement(By.id("id"));
                    }
                    catch (NoSuchElementException e )
                    {
                        e.printStackTrace();
                        id = null;
                    }
                    return id != null;
                }
            };
            result = wait.until(condition);
        }
        catch( org.openqa.selenium.TimeoutException e)
        {
            result = false;
        }
        driver.quit();
        return result;
    }

    @Before
    public void setUp() throws Exception {
        server = ServerConfigurator.ConfigureServer(Constants.TEST_PORT);
        server.start();
        accountService = new AccountService();
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(10);
        accountService.addUser(login , password);
    }

    @Test
    public void loginTestSuccess() throws Exception {
        Assert.assertTrue(testLogin("http://localhost:" + Constants.TEST_PORT + Constants.Url.AUTHFORM, login , password));
    }

    @Test
    public void loginTestFail() throws Exception {
        Assert.assertFalse(testLogin("http://localhost:" + Constants.TEST_PORT + Constants.Url.AUTHFORM, password, login));
    }

    @After
    public void tearDown() throws Exception {
        accountService.deleteUser(login);
        server.stop();
    }
}