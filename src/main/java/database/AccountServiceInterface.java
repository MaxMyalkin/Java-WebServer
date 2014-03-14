package database;

/*
 * Created by maxim on 14.03.14.
 */
public interface AccountServiceInterface {
    public boolean addUser(String login , String password);
    public boolean checkUser(String login , String password);
    public boolean checkLogin(String login);
    public boolean deleteUser(String login);
}
