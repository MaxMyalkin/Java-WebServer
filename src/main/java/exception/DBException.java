package exception;

/*
 * Created by maxim on 12.04.14.
 */

public class DBException extends Exception {

    public DBException() {
        super("database exception");
    }

    public DBException(String message, Throwable cause) {
        super(message,cause);
    }

    public DBException(Throwable cause) {
        super("database exception",cause);
    }
}
