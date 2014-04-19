package database;

import resources.DBSettings;
import org.hibernate.cfg.Configuration;
import resources.ResourceFactory;

/*
 * Created by maxim on 15.03.14.
 */
public class DBService extends DataService {
    protected void setConfiguration(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url",
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getDBUrl());
        configuration.setProperty("hibernate.connection.username",
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getUsername());
        configuration.setProperty("hibernate.connection.password",
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getPassword());
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
    }
}
