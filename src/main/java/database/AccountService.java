package database;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import thread.Helper;

public class AccountService implements Runnable, Abonent {

    private UsersDataSetDAO dao;
    private MessageSystem messageSystem;
    private Address address;

    public AccountService(DataService dataService , MessageSystem messageSystem){
        this.dao = new UsersDataSetDAO(dataService);
        this.messageSystem = messageSystem;
        this.address = new Address();
        this.messageSystem.addAbonent(this);
        this.messageSystem.getAddressService().setAccountService(address);
    }

    public UsersDataSet getUser(String login, String password){
        UsersDataSet user = dao.getByLogin(login);
        if(user != null && user.getPassword().equals(password))
            return user;
        else
            return null;
    }

    public boolean addUser(String login , String password) {
        if(!checkLogin(login)){
            dao.add( new UsersDataSet(login, password));
            return true;
        }
        else
            return false;
    }

    public boolean checkUser(String login , String password){
        try {
           return dao.getByLogin(login).getPassword().equals(password);
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkLogin(String login){
        return dao.getByLogin(login) != null;
    }

    public boolean deleteUser(String login){
        return dao.delete(login);
    }

    public void run(){
        while (true){
            messageSystem.execForAbonent(this);
            Helper.sleep(300);
        }
    }

    public Address getAddress(){
        return address;
    }

    public MessageSystem getMessageSystem(){
        return messageSystem;
    }

}
