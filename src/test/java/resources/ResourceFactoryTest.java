package resources;

import junit.framework.Assert;
import org.junit.Test;

/*
 * Created by maxim on 19.04.14.
 */

public class ResourceFactoryTest {

    @Test
    public void testGet() throws Exception {
        Resource resource = ResourceFactory.instance().get("page.xml");
        Assert.assertNotNull(resource);
    }
}
