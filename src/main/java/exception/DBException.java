package exception;

/*
 * Created by maxim on 12.04.14.
 */

public class DBException extends Exception {

    public DBException() {
        super("database exception");
    }

    public DBException(Throwable cause) {
        super("database exception",cause);
    }
}
