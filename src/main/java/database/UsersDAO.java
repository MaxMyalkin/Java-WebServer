package database;

import frontend.CreatedBy;


@CreatedBy(name = "max" , date = "01.03.14")
public interface UsersDAO {

    UsersDataSet getByLogin(String login);
    void add(UsersDataSet usersDataSet);
    boolean delete(String login);

}
