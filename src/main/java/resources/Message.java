package resources;

/*
 * Created by maxim on 19.04.14.
 */
public class Message implements Resource {
    private String emptyFields;
    private String userExists;
    private String successfulRegistration;
    private String authFailed;
    private String authSuccessful;
    private String waiting;
    private String databaseError;
    private String needAuth;

    public String getEmptyFields() {
        return emptyFields;
    }

    public String getUserExists() {
        return userExists;
    }

    public String getSuccessfulRegistration() {
        return successfulRegistration;
    }

    public String getAuthFailed() {
        return authFailed;
    }

    public String getAuthSuccessful() {
        return authSuccessful;
    }

    public String getWaiting() {
        return waiting;
    }

    public String getDatabaseError() {
        return databaseError;
    }

    public String getNeedAuth() {
        return needAuth;
    }
}
