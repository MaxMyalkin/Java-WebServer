package frontend;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

/*
 * Created by maxim on 07.03.14.
 */
public class PageGeneratorTest {
    @Test
    public void testGetPage() throws Exception {
        String template = Constants.Page.INDEX;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Индекс"));
        template = Constants.Page.AUTHORIZATION;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Форма авторизации"));
        template = Constants.Page.REGISTRATION;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Форма регистрации"));
        template = Constants.Page.SESSION;
        Assert.assertTrue(PageGenerator.getPage(template , new HashMap<String, Object>()).contains("Сессия"));
    }
}
