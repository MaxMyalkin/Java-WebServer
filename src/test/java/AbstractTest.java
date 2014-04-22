import database.AccountService;
import database.TestDBService;
import helpers.CommonHelper;
import resources.DBSettings;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import resources.ResourceFactory;
import server.ServerConfigurator;

/*
 * Created by maxim on 15.03.14.
 */
public abstract class AbstractTest {
    protected AccountService accountService;
    protected String login;
    protected String password;
    protected Server server;

    @Before
    public void before() throws Exception{
        server = ServerConfigurator.ConfigureServer(((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestPort());
        server.start();
        accountService = new AccountService(new TestDBService(), new MessageSystem(new AddressService()));
        login = CommonHelper.getRandomString(10);
        password = CommonHelper.getRandomString(10);
    }

    @After
    public void after() throws Exception{
        server.stop();
        accountService.deleteUser(login);
    }

    void setElements( WebDriver driver , String username , String password) {
        WebElement loginEl = driver.findElement(By.name("login"));
        loginEl.sendKeys(username);
        WebElement passwordEl = driver.findElement(By.name("password"));
        passwordEl.sendKeys(password);
        WebElement btnElement = driver.findElement(By.className("btn-submit"));
        btnElement.submit();
    }
}
