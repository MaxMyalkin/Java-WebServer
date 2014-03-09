package database;


import frontend.CreatedBy;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@CreatedBy( name = "max" , date = "01.03.14" )
public class UsersDataSetDAO implements UsersDAO{
    private SessionFactory sessionFactory;

    public UsersDataSetDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public UsersDataSet getByLogin(String login) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        UsersDataSet userDataSet = (UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
        transaction.commit();
        session.close();
        return userDataSet;
    }

    @Override
    public void add(UsersDataSet usersDataSet) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(usersDataSet);
        transaction.commit();
        session.close();
    }

    @Override
    public boolean delete(String login) {
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
