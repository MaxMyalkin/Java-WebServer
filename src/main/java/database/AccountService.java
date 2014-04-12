package database;

import exception.DBException;
import frontend.Constants;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

public class AccountService implements Runnable, Abonent {

    private final UsersDataSetDAO dao;
    private final MessageSystem messageSystem;
    private final Address address;

    public AccountService(DataService dataService , MessageSystem messageSystem){
        this.dao = new UsersDataSetDAO(dataService);
        this.messageSystem = messageSystem;
        this.address = new Address();
        this.messageSystem.addAbonent(this.getClass(), this);
        dataService.getSessionFactory().close();
    }

    public UsersDataSet getUser(String login, String password) throws DBException{
        System.out.println(address);
        UsersDataSet user = dao.getByLogin(login);
        if(user != null && user.getPassword().equals(password))
            return user;
        else
            return null;
    }

    public boolean addUser(String login , String password) throws DBException{
        if(!checkLogin(login)){
            dao.add( new UsersDataSet(login, password));
            return true;
        }
        else
            return false;
    }

    public boolean checkUser(String login , String password) throws DBException{
        try {
           return dao.getByLogin(login).getPassword().equals(password);
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkLogin(String login) throws DBException{
        return dao.getByLogin(login) != null;
    }

    public boolean deleteUser(String login) throws DBException{
        return dao.delete(login);
    }

    public void run(){
        while (true){
            Constants.sleep(300);
            messageSystem.execForAbonent(this);
        }
    }

    public Address getAddress(){
        return address;
    }

    public MessageSystem getMessageSystem(){
        return messageSystem;
    }

}
