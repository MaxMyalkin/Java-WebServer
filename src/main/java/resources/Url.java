package resources;

/*
 * Created by maxim on 19.04.14.
 */
public class Url implements Resource {
    private String authform;
    private String registerform;
    private String session;
    private String index;
    private String logout;
    private String ajaxChecking;

    public String getAuthform() {
        return authform;
    }

    public String getRegisterform() {
        return registerform;
    }

    public String getSession() {
        return session;
    }

    public String getIndex() {
        return index;
    }

    public String getLogout() {
        return logout;
    }

    public String getAjaxChecking() {
        return ajaxChecking;
    }
}
