package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String PROP_PATH="src/main/resources/connection.properties";
    private static Connection connection;
    private static Properties properties = new Properties();

    private DBConnection() {
    }

    public static Connection buildConnection() throws IOException, SQLException {
        if (properties.isEmpty()) {
            properties = new Properties();
            properties.load(new FileInputStream(PROP_PATH));
        }
        if (connection == null) {
            connection = DriverManager.getConnection(properties.getProperty("connection.url"),
                                                     properties.getProperty("connection.user"),
                                                     properties.getProperty("connection.password"));
        }
        return connection;
    }

    public static boolean closeConnection() {
        if (connection==null) {
            return false;
        }
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
