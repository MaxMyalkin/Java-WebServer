package database;

import frontend.CreatedBy;

import javax.persistence.*;
import java.io.Serializable;

@CreatedBy(name = "max" , date = "01.03.14")
@Entity
@Table(name = "users")
public class UsersDataSet implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public UsersDataSet(String login) {
        this.setId(-1);
        this.setLogin(login);
    }

    public UsersDataSet(String login, String password) {
        this.setId(-1);
        this.setLogin(login);
        this.setPassword(password);
    }

    public UsersDataSet(long id, String login, String password) {
        this.setId(id);
        this.setLogin(login);
        this.setPassword(password);
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public long getId() {
        return this.id;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

}
