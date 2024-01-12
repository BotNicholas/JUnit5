package connection;

import static org.junit.jupiter.api.Assertions.*;

import DAO.AuthorDao;
import objects.Author;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class ConnectionTest {
    static Author testAuthor;

    @BeforeAll
    public static void setUp(){
        testAuthor = new Author(999, Date.valueOf("1990-3-22"), "TEST CONTACT DETAILS", "TEST", 'M', "TA", "AUTHOR", "TEST OTHER DETAILS");
    }


    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        ConnectionManager.buildConnection();
        System.out.println("\tConnection for test is opened...");
    }

    @AfterEach
    public void afterEach() throws SQLException {
        ConnectionManager.closeConnection();
        System.out.println("\tConnection for test is closed...");
    }

    @Test
    @DisplayName("Opening connection")
    public void openConnection() throws IOException, SQLException {
        System.out.println("Obtaining connection...");
        Connection connection = ConnectionManager.buildConnection();
        assertNotNull(connection, "connection is not obtained! It is null!");
        System.out.println("Connection's obtained!");
        connection.close();
    }

    @Test
    @DisplayName("Closing connection")
    public void closeConnection() throws SQLException, IOException {
        //Checking if connection is initialized
        //Connection is set up in before each method!!!
        System.out.println("Attempting to close the connection...");
        assertDoesNotThrow(ConnectionManager::closeConnection, "Connection can not be closed!");
        System.out.println("Connection's closed!");
    }

    @Test
    @DisplayName("Obtaining Statement")
    public void getStatementTest(){
        System.out.println("Obtaining statement...");
        assertDoesNotThrow(ConnectionManager::getStatement, "Statement has not been obtained!");
        try {
            assertNotNull(ConnectionManager.getStatement(), "Statement has not been obtained!");
            System.out.println("Statement obtained...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Preparing statement without parameters")
    public void prepareStatementWithoutParametersTest(){
        System.out.println("Preparing statement...");
        Assertions.assertDoesNotThrow(()->ConnectionManager.prepareStatement("select from authors where id = ?"), "Preparing the statement failed!");
        try {
            Assertions.assertNotNull(ConnectionManager.prepareStatement("select from authors where id = ?"), "Preparing the statement failed!");
            System.out.println("Prepared statement obtained...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Preparing statement with parameters")
    public void prepareStatementWithParametersTest(){
        System.out.println("Preparing statement...");
        Assertions.assertDoesNotThrow(()->ConnectionManager.prepareStatement("select from authors where id = ?", testAuthor.getId()), "Preparing the statement failed!");
        try {
            Assertions.assertNotNull(ConnectionManager.prepareStatement("select from authors where id = ?", testAuthor.getId()), "Preparing the statement failed!");
            System.out.println("Prepared statement obtained...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Preparing statement from object")
    public void prepareStatementFromObjectTest(){
        System.out.println("Preparing statement...");
        Assertions.assertDoesNotThrow(() -> ConnectionManager.prepareStatementFromObject("select from authors where id = ?", testAuthor), "Preparing the statement failed!");
        try {
            Assertions.assertNotNull(ConnectionManager.prepareStatementFromObject("select from authors where id = ?", testAuthor), "Preparing the statement failed!");
            System.out.println("Prepared statement obtained...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    @DisplayName("Updating increment for Authors table")
    public void updateIncrementForAuthorTableTest(){
        System.out.println("Attempting to update AUTO_INCREMENT...");
        Assertions.assertDoesNotThrow(() -> ConnectionManager.updateIncrementForTable("authors"), "Increment was not updated!");
        System.out.println("AUTO_INCREMENT has been updated...");
    }
}
