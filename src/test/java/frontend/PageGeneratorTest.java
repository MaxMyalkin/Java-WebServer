package frontend;

import junit.framework.Assert;
import org.junit.Test;


/*
 * Created by maxim on 07.03.14.
 */
public class PageGeneratorTest {
    @Test
    public void testGetIndexPage() throws Exception {
        String template = "/index.html";
        Assert.assertTrue(PageGenerator.getPage(template , null).contains("Индекс"));
    }

    @Test
    public void testGetAuthPage() throws Exception {
        String template = "/authform.html";
        Assert.assertTrue(PageGenerator.getPage(template , null).contains("Форма авторизации"));
    }

    @Test
    public void testGetRegisterPage() throws Exception {
        String template = "/registerform.html";
        Assert.assertTrue(PageGenerator.getPage(template , null).contains("Форма регистрации"));
    }

    @Test
    public void testGetSessionPage() throws Exception {
        String template = "/userId.html";
        Assert.assertTrue(PageGenerator.getPage(template , null).contains("Сессия"));
    }

}
