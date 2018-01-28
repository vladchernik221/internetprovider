package test.com.chernik.util;

import com.ibatis.common.jdbc.ScriptRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RepositoryIntegrationTest {
    private static final String PROPERTY_FILE_NAME = "application";
    private static final String DATABASE_CONNECTION_FORMAT = "%s?&verifyServerCertificate=false&useSSL=true&serverTimezone=UTC";
    private static final String URL_PROPERTY_NAME = "database.url";
    private static final String USER_PROPERTY_NAME = "database.user";
    private static final String PASSWORD_PROPERTY_NAME = "database.password";
    private static final String CREATE_SQL_SCRIPT_FORMAT = "db/script/%sCreate.sql";
    private static final String CLEAR_DATABASE = "db/script/ClearDatabase.sql";

    private Connection connection;

    private String dataScriptName;

    public RepositoryIntegrationTest(String dataScriptName) {
        this.dataScriptName = dataScriptName;
    }

    @BeforeClass
    public void configConnection() throws SQLException {
        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTY_FILE_NAME);

        String url = bundle.getString(URL_PROPERTY_NAME);
        String user = bundle.getString(USER_PROPERTY_NAME);
        String password = bundle.getString(PASSWORD_PROPERTY_NAME);

        connection = DriverManager.getConnection(String.format(DATABASE_CONNECTION_FORMAT, url), user, password);
    }

    @BeforeMethod
    public void setUp() throws IOException, SQLException, URISyntaxException {
        tearDown();
        ScriptRunner runner = new ScriptRunner(connection, false, true);
        Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(String.format(CREATE_SQL_SCRIPT_FORMAT, dataScriptName)).toURI());
        Reader reader = new FileReader(path.toFile());

        runner.runScript(reader);
    }

    @AfterMethod
    public void tearDown() throws IOException, SQLException, URISyntaxException {
        ScriptRunner runner = new ScriptRunner(connection, false, true);
        Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(CLEAR_DATABASE).toURI());
        Reader reader = new FileReader(path.toFile());

        runner.runScript(reader);
    }
}
