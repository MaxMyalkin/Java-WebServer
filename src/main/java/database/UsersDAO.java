package database;


import frontend.CreatedBy;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@CreatedBy( name = "max" , date = "01.03.14" )
public class UsersDAO {
    private SessionFactory sessionFactory;

    public UsersDAO (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(UsersDataSet dataSet) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(dataSet);
        transaction.commit();
        session.close();
    }

}
