package connection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    @Test
    @DisplayName("Opening connection")
    public void openConnection() throws IOException, SQLException {
        System.out.println("Obtaining connection...");
        Connection connection = DBConnection.buildConnection();
        assertNotNull(connection, "connection is not obtained! It is null!");
        System.out.println("Connection's obtained!");
        connection.close();
    }

    @Test
    @DisplayName("closing connection")
    public void closeConnection() throws SQLException, IOException {
        //Checking if connection is initialized
        System.out.println("Checking the connection...");
        assertNotNull(DBConnection.buildConnection(), "Connection is not initialized! there is nothing to close");
        System.out.println("Attempting to close the connection...");
        assertTrue(DBConnection.closeConnection(), "Connection can not be closed!");
        System.out.println("Connection's closed!");
    }
}
