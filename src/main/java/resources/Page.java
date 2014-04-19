package resources;

/*
 * Created by maxim on 19.04.14.
 */
public class Page implements Resource {
    private String index;
    private String registration;
    private String authorization;
    private String session;

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
}
