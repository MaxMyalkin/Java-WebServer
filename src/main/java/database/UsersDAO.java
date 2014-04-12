package database;

import exception.DBException;
import frontend.CreatedBy;


@CreatedBy(name = "max" , date = "01.03.14")
public interface UsersDAO {

    UsersDataSet getByLogin(String login) throws DBException;
    void add(UsersDataSet usersDataSet) throws DBException;
    boolean delete(String login) throws DBException;

}
