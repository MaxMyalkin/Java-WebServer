package helpers;

/*
 * Created by maxim on 19.04.14.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonHelper {
    public static void sleep(int ms){
        try {
            Thread.sleep(ms);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRandomString( int length) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < length ; ++i )
        {
            string.append((char)(Math.random()*(128 - 32) + 32));
        }
        return string.toString();
    }

    final private static DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");

    public static String getTime() {
        return FORMATTER.format(new Date());
    }

}
