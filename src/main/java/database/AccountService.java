package database;

import frontend.CreatedBy;
import java.sql.SQLException;

@CreatedBy(name = "max" , date = "22.02.14")
public class AccountService {

    private UsersDataSetDAO dao;

    public AccountService() {
        this.dao = new UsersDataSetDAO();
    }

    public boolean addUser(String login , String password) {
        try {
            if(!checkLogin(login))
            {
                dao.add( new UsersDataSet(login, password));
                return true;
            }
            else
                return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkUser(String login , String password)
    {
        try {
           return dao.getByLogin(login).getPassword().equals(password);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkLogin(String login)
    {
        try {
            return dao.getByLogin(login) != null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(String login)
    {
        try {
            dao.delete(login);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}