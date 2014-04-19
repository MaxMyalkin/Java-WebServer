import com.sun.istack.internal.NotNull;
import resources.DBSettings;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.Message;
import resources.ResourceFactory;
import resources.Url;

import java.util.NoSuchElementException;

/*
 * Created by maxim on 09.03.14.
 */
public class RegisterTest extends AbstractTest {

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
                    WebElement msgEl;
                    String message;
                    try
                    {
                        msgEl = d.findElement(By.id("info"));
                        message = msgEl.getText();
                    }
                    catch (NoSuchElementException e )
                    {
                        e.printStackTrace();
                        message = "";
                    }
                    return message.equals(((Message) ResourceFactory.instance().get("message.xml")).getSuccessfulRegistration());
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


    //@Test
    public void registrationTestSuccess() throws Exception {
        Assert.assertTrue(testLogin("http://localhost:" +
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestPort() +
                ((Url) ResourceFactory.instance().get("url.xml")).getRegisterform(), login, password));
    }

    //@Test
    public void registrationTestFail() throws Exception {
        accountService.addUser(login , password);
        Assert.assertFalse(testLogin("http://localhost:" +
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestPort() +
                ((Url) ResourceFactory.instance().get("url.xml")).getRegisterform(), login, password));
    }
}