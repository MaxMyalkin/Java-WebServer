import VFS.VFS;
import resources.DBSettings;
import org.eclipse.jetty.server.Server;
import resources.ResourceFactory;
import server.ServerConfigurator;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception {
        VFS vfs = new VFS("");
        Iterator<String> iterator = vfs.getIterator("data");
        while(iterator.hasNext()) {
            String nextIter = iterator.next();
            if(!vfs.isDirectory(nextIter))
                ResourceFactory.instance().putResource(nextIter,
                    ResourceFactory.instance().getResource(vfs.getAbsolutePath(nextIter)));
        }

        Server server = ServerConfigurator.ConfigureServer(((DBSettings)ResourceFactory.instance().get("dbSettings.xml")).getPort());
        server.start();
        server.join();
    }

}
