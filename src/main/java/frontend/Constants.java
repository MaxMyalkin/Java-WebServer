package frontend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by maxim on 09.03.14.
 */
public class Constants {

    final static public String REFRESH_TIME = "2000";

    static public String getRandomString( int length) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < length ; ++i )
        {
            string.append((char)(Math.random()*(128 - 32) + 32));
        }
        return string.toString();
    }

    final static private DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");

    public static String getTime() {
        return FORMATTER.format(new Date());
    }




}
