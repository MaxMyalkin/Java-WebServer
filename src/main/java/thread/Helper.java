package thread;

/*
 * Created by maxim on 29.03.14.
 */
public class Helper {
    public static void sleep(int ms){
        try {
            Thread.sleep(ms);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
