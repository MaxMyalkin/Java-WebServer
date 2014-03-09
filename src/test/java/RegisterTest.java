import com.sun.istack.internal.NotNull;
import database.AccountService;
import frontend.Constants;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;

/*
 * Created by maxim on 09.03.14.
 */
public class RegisterTest {
    private AccountService accountService;
    private String login;
    private String password;

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
                    WebElement msgEl;
                    String message;
                    try
                    {
                        msgEl = d.findElement(By.className("error"));
                        message = msgEl.getText();
                    }
                    catch (NoSuchElementException e )
                    {
                        e.printStackTrace();
                        message = "";
                    }
                    return message.equals("User was added");
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
        accountService = new AccountService();
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(10);
    }

    @Test
    public void registrationTestSuccess() throws Exception {
        Assert.assertTrue(testLogin("http://localhost:8800" + Constants.Url.REGISTERFORM, login , password));
    }

    @Test
    public void registrationTestFail() throws Exception {
        accountService.addUser(login , password);
        Assert.assertFalse(testLogin("http://localhost:8800" + Constants.Url.REGISTERFORM, login, password));
    }

    @After
    public void tearDown() throws Exception {
        accountService.deleteUser(login);
    }
}