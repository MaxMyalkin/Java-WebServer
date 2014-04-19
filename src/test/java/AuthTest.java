import com.sun.istack.internal.NotNull;
import resources.DBSettings;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.ResourceFactory;
import resources.Url;

import java.util.NoSuchElementException;

/*
 * Created by maxim on 08.03.14.
 */
public class AuthTest extends AbstractTest {

    public boolean testLogin(@NotNull String url,@NotNull String username,@NotNull String password) {

        WebDriver driver = new HtmlUnitDriver(true);
        driver.get(url);
        setElements(driver , username , password);
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
        accountService.addUser(login , password);
    }

    @Test
    public void loginTestSuccess() throws Exception {
        Assert.assertTrue(testLogin("http://localhost:" +
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestPort() +
                ((Url) ResourceFactory.instance().get("url.xml")).getAuthform(), login , password));
    }

    @Test
    public void loginTestFail() throws Exception {
        Assert.assertFalse(testLogin("http://localhost:" +
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestPort() +
                ((Url) ResourceFactory.instance().get("url.xml")).getAuthform(), password, login));
    }

}