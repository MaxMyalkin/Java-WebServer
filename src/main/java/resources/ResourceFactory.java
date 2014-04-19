package resources;

import sax.Sax;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by maxim on 19.04.14.
 */
public class ResourceFactory {
    static private ResourceFactory instance;
    private Map<String, Resource> resourceMap = new HashMap<>();

    private ResourceFactory() {

    }

    static public ResourceFactory instance() {
        if(instance == null)
            instance = new ResourceFactory();
        return instance;
    }

    public Resource getResource(String path) {
        return (Resource)Sax.readXML(path);
    }

    public void putResource(String str, Resource resource) {
        resourceMap.put(str,resource);
    }

    public Resource get(String key) {
        Resource resource = resourceMap.get("data/" + key);
        if(resource == null) {
            putResource("data/" + key, (Resource)Sax.readXML("data/" + key));
            resource = resourceMap.get("data/" + key);
        }
        return resource;
    }
}
