package database;


import exception.DBException;
import frontend.CreatedBy;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@CreatedBy( name = "max" , date = "01.03.14" )
public class UsersDataSetDAO implements UsersDAO{
    private final SessionFactory sessionFactory;

    public UsersDataSetDAO(DataService dataService) {
        this.sessionFactory = dataService.getSessionFactory();
    }

    @Override
    public UsersDataSet getByLogin(String login) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(UsersDataSet.class);
            UsersDataSet userDataSet = (UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
            transaction.commit();
            session.close();
            return userDataSet;
        }
        catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public void add(UsersDataSet usersDataSet) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(usersDataSet);
            transaction.commit();
            session.close();
        }
        catch (Exception e){
            throw new DBException(e);
        }
    }

    @Override
    public boolean delete(String login) throws DBException {
        Session session = sessionFactory.openSession();
        UsersDataSet usersDataSet = getByLogin(login);
        if(usersDataSet != null)
        {
            Transaction transaction = session.beginTransaction();
            session.delete(usersDataSet);
            transaction.commit();
            session.close();
            return true;
        }
        else
        {
            session.close();
            return false;
        }

    }
}
