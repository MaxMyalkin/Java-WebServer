package helpers;

/*
 * Created by maxim on 19.04.14.
 */

public class TimeHelper {
    public static void sleep(int ms){
        try {
            Thread.sleep(ms);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
