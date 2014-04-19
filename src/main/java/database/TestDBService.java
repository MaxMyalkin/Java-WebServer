package database;

import resources.DBSettings;
import org.hibernate.cfg.Configuration;
import resources.ResourceFactory;

/*
 * Created by maxim on 15.03.14.
 */
public class TestDBService extends DataService {
    protected void setConfiguration(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url",
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestDBUrl());
        configuration.setProperty("hibernate.connection.username",
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestUsername());
        configuration.setProperty("hibernate.connection.password",
                ((DBSettings) ResourceFactory.instance().get("dbSettings.xml")).getTestPassword());
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }
}
