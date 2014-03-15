import frontend.Constants;
import frontend.CreatedBy;
import org.eclipse.jetty.server.Server;
import server.ServerConfigurator;

@CreatedBy(name = "max" , date = "01.03.14")
public class Main {
    public static void main(String[] args) throws Exception {

        Server server = ServerConfigurator.ConfigureServer(Constants.MAIN_PORT);
        server.start();
        server.join();
    }

}
