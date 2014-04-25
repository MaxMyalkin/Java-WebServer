import VFS.VFS;
import resources.DBSettings;
import org.eclipse.jetty.server.Server;
import resources.ResourceFactory;
import server.ServerConfigurator;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerConfigurator.loadResources("data");
        Server server = ServerConfigurator.ConfigureServer(((DBSettings)ResourceFactory.instance().get("dbSettings.xml")).getPort());
        server.start();
        server.join();
    }
}
