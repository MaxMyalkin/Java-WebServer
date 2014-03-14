package database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory TEST_SESSION_FACTORY = buildTestSessionFactory();
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildTestSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        setTestConfiguration(configuration);
        builder.applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        setConfiguration(configuration);
        builder.applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }


    public static SessionFactory getSessionFactory(boolean isTestedDB) {
        if(isTestedDB)
            return TEST_SESSION_FACTORY;
        else
            return SESSION_FACTORY;
    }

    private static void setTestConfiguration(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javaTestDB");
        configuration.setProperty("hibernate.connection.username", "maxim");
        configuration.setProperty("hibernate.connection.password", "12345");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }

    private static void setConfiguration(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javadb");
        configuration.setProperty("hibernate.connection.username", "maxim");
        configuration.setProperty("hibernate.connection.password", "12345");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
    }

}