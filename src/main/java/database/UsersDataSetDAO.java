package database;


import frontend.CreatedBy;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import java.sql.SQLException;

@CreatedBy( name = "max" , date = "01.03.14" )
public class UsersDataSetDAO implements UsersDAO{
    private SessionFactory sessionFactory;

    public UsersDataSetDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public UsersDataSet getByLogin(String login) throws SQLException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        UsersDataSet userDataSet = (UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
        session.close();
        return userDataSet;
    }

    @Override
    public void add(UsersDataSet usersDataSet) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(usersDataSet);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(String login) throws SQLException {
        Session session = sessionFactory.openSession();
        UsersDataSet usersDataSet = getByLogin(login);
        Transaction transaction = session.beginTransaction();
        session.delete(usersDataSet);
        transaction.commit();
        session.close();
    }
}
