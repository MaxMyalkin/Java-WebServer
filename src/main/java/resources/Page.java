package resources;

/*
 * Created by maxim on 19.04.14.
 */
@SuppressWarnings("UnusedDeclaration")
public class Page implements Resource {
    private String index;
    private String registration;
    private String authorization;
    private String session;
    private String refreshTime;

    public String getIndex() {
        return index;
    }

    public String getRegistration() {
        return registration;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getSession() {
        return session;
    }

    public String getRefreshTime() {
        return refreshTime;
    }
}
