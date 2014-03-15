package database;

import org.hibernate.cfg.Configuration;

/*
 * Created by maxim on 15.03.14.
 */
public class TestDBService extends DataService {
    protected void setConfiguration(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javaTestDB");
        configuration.setProperty("hibernate.connection.username", "maxim");
        configuration.setProperty("hibernate.connection.password", "12345");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }
}
