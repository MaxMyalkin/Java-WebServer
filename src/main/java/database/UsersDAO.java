package database;

import frontend.CreatedBy;

import java.sql.SQLException;

@CreatedBy(name = "max" , date = "01.03.14")
public interface UsersDAO {

    UsersDataSet getByLogin(String login) throws SQLException;
    void add(UsersDataSet usersDataSet) throws SQLException;
    void delete( String login) throws SQLException;

}
