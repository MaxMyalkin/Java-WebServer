package frontend;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

/*
 * Created by maxim on 07.03.14.
 */
public class PageGeneratorTest {
    @Test
    public void testGetIndexPage() throws Exception {
        String template = Constants.Page.INDEX;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Индекс"));
    }

    @Test
    public void testGetAuthPage() throws Exception {
        String template = Constants.Page.AUTHORIZATION;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Форма авторизации"));
    }

    @Test
    public void testGetRegisterPage() throws Exception {
        String template = Constants.Page.REGISTRATION;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Форма регистрации"));
    }

    @Test
    public void testGetSessionPage() throws Exception {
        String template = Constants.Page.SESSION;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Сессия"));
    }

}
