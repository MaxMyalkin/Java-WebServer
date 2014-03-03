package database;

import frontend.CreatedBy;
import java.sql.SQLException;

@CreatedBy(name = "max" , date = "22.02.14")
public class AccountService {

    private UsersDataSetDAO dao;

    public AccountService() {
        this.dao = new UsersDataSetDAO();
    }

    public void addUser(String login , String password) {
        try {
            dao.add( new UsersDataSet(login, password));
        }
        catch (SQLException e) {
            e.printStackTrace();
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
        //return false;
    }

    public boolean checkLogin(String login)
    {
        try {
            return dao.getByLogin(login) != null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
