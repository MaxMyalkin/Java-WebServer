package database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/*
 * Created by maxim on 15.03.14.
 */
public abstract class DataService {
    private SessionFactory sessionFactory = buildSessionFactory();
    private SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        setConfiguration(configuration);
        builder.applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    abstract void setConfiguration(Configuration configuration);
}
