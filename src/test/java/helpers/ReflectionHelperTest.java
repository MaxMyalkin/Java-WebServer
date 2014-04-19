package helpers;

import junit.framework.Assert;
import org.junit.Test;

/*
 * Created by maxim on 19.04.14.
 */
public class ReflectionHelperTest {

    @Test
    public void testCreateIntance() throws Exception {
        String className = "helpers.SomeClass";
        Object object = ReflectionHelper.createIntance(className);
        Assert.assertEquals(object.getClass(), SomeClass.class);
    }

    @Test
    public void testSetFieldValue() throws Exception {
        SomeClass object = new SomeClass();
        ReflectionHelper.setFieldValue(object, "second", "10");
        ReflectionHelper.setFieldValue(object, "first", "jjj");
        Assert.assertEquals(object.getSecond(), 10);
        Assert.assertEquals(object.getFirst(), "jjj");

    }
}
