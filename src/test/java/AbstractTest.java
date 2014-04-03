import database.AccountService;
import database.TestDBService;
import frontend.Constants;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        server = ServerConfigurator.ConfigureServer(Constants.TEST_PORT);
        server.start();
        accountService = new AccountService(new TestDBService(), new MessageSystem(new AddressService()));
        login = Constants.getRandomString(10);
        password = Constants.getRandomString(10);
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
