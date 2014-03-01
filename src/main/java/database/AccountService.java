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
           UsersDataSet usersDataSet = (UsersDataSet) dao.getByLogin(login , password);
            return usersDataSet != null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
