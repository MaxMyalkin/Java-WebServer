package frontend;

/*
 * Created by maxim on 09.03.14.
 */
public class Constants {
    public class Url {
        final static public String AUTHFORM = "/authform";
        final static public String REGISTERFORM = "/registerform";
        final static public String SESSION = "/userid";
        final static public String INDEX = "/index";
    }

    public class Page {
        final static public String INDEX = "/index.html";
        final static public String REGISTRATION = "/registerform.html";
        final static public String AUTHORIZATION = "/authform.html";
        final static public String SESSION = "/userId.html";
    }

    public class Message {
        final static public String EMPTY_FIELDS = "Input all fields";
        final static public String USER_EXISTS = "User already exists";
        final static public String SUCCESSFUL_REGISTRATION = "User was added";
        final static public String AUTH_FAILED = "Wrong login/password";
    }
    final static public String REFRESH_TIME = "5000";
    public static String getRandomString( int length) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < length ; ++i )
        {
            string.append((char)(Math.random()*(128 - 32) + 32));
        }
        return string.toString();
    }
}
