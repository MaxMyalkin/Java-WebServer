package sax;

import resources.SomeClass;
import junit.framework.Assert;
import org.junit.Test;

/*
 * Created by maxim on 19.04.14.
 */

public class SaxTest {

    @Test
    public void testReadXMLSuccess() throws Exception {
        SomeClass someClass = (SomeClass)Sax.readXML("data/someData.xml");
        Assert.assertNotNull(someClass);
        Assert.assertEquals(someClass.getFirst(),"qwerty" );
        Assert.assertEquals(someClass.getSecond(),123);
    }

    @Test
    public void testReadXMLFail() throws Exception {
        Object object = Sax.readXML("data/dbSettingsBad.xml");
        Assert.assertNull(object);
    }


}
