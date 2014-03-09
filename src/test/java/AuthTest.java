import com.sun.istack.internal.NotNull;
import database.AccountService;
import frontend.Url;
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

/*
 * Created by maxim on 08.03.14.
 */
public class AuthTest {
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


        boolean result = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            @NotNull
            public Boolean apply(@NotNull WebDriver d) {
                final WebElement id = d.findElement(By.id("id"));
                String textID = id.getText();
                return textID!= null;
            }

        });
        driver.quit();
        return result;
    }

    private String getRandomString( int length) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < length ; ++i )
        {
            string.append((char)(Math.random()*(255 - 32) + 32));
        }
        return string.toString();
    }

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
        login = getRandomString(10);
        password = getRandomString(10);
        accountService.addUser("123" , "123");
    }

    @Test
    public void loginTest() throws Exception {
        Assert.assertTrue(testLogin("http://localhost:8800" + Url.AUTHFORM, "123" , "123"));
    }

    @After
    public void tearDown() throws Exception {
        accountService.deleteUser("123");
    }
}