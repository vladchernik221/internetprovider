package com.chernik.internetprovider.persistence;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Component;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.util.ResourceBundle;

@Component
public class DatabaseMigration {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseMigration.class);

    private static final String PROPERTY_FILE_NAME = "application";
    private static final String URL_PROPERTY_NAME = "database.url";
    private static final String USER_PROPERTY_NAME = "database.user";
    private static final String PASSWORD_PROPERTY_NAME = "database.password";

    private String url;
    private String user;
    private String password;

    public DatabaseMigration() {
        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTY_FILE_NAME);

        url = bundle.getString(URL_PROPERTY_NAME);
        user = bundle.getString(USER_PROPERTY_NAME);
        password = bundle.getString(PASSWORD_PROPERTY_NAME);
    }

    @AfterCreate
    public void migrate() {
        Flyway flyway = new Flyway();

        LOGGER.log(Level.DEBUG, "Start flyway migration for database with URL {} and user {}", url, user);
        flyway.setDataSource(url, user, password);
        flyway.migrate();
    }
}
