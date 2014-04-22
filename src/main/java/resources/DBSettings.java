package resources;

/*
 * Created by maxim on 19.04.14.
 */

@SuppressWarnings("UnusedDeclaration")
public class DBSettings implements Resource {
    private String dbUrl;
    private String testDBUrl;
    private int port;
    private int testPort;
    private String username;
    private String password;
    private String testUsername;
    private String testPassword;

    public String getDBUrl() {
        return dbUrl;
    }

    public String getTestDBUrl() {
        return testDBUrl;
    }

    public int getPort() {
        return port;
    }

    public int getTestPort() {
        return testPort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTestUsername() {
        return testUsername;
    }

    public String getTestPassword() {
        return testPassword;
    }
}
