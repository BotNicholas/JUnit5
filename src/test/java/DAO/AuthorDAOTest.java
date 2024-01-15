package DAO;

import com.opencsv.exceptions.CsvException;
import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
import objects.Author;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static DAO.Constants.CSV_OUTPUT_PATH;

public class AuthorDAOTest {
    static AuthorDao dao;
    static Author author;

    @BeforeAll
    public static void setup(){
        try {
            ConnectionManager.buildConnection();
            dao = new AuthorDao();
            author = new Author(999, Date.valueOf("1990-3-22"), "TEST CONTACT DETAILS", "TEST", 'M', "TA", "AUTHOR", "TEST OTHER DETAILS");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void cleanup(){
        try {
            ConnectionManager.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Find all authors")
    public void findAllAuthors(){
        List<Author> authors = dao.findAll();
        Assertions.assertNotNull(authors);
        Assertions.assertFalse(authors.isEmpty(), "Authors list is empty");

//        AuthorDao spy = Mockito.spy(dao);
//        Mockito.when(spy.findAll()).thenThrow(SQLException.class);
//        spy.findAll();
    }

    @Test
    @DisplayName("Find one existing author")
    public void findValidAuthor(){
        Optional<Author> authorOptional = dao.findByKey(1);
        Assertions.assertTrue(authorOptional.isPresent());
        Assertions.assertDoesNotThrow(authorOptional::get, "Object should be obtained!");
    }

    @ParameterizedTest
    @ValueSource(ints = {999999, -1, 0})
    @DisplayName("Find non-existing authors")
    public void findNonExistingAuthors(Integer key){
        Optional<Author> authorOptional = dao.findByKey(key);
        Assertions.assertFalse(authorOptional.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, authorOptional::get, "NoSuchElementException should be thrown!");
    }

    @Test
    @DisplayName("Save new author")
    public void saveAuthor(){
        Assertions.assertTrue(dao.findByKey(author.getId()).isEmpty(), "Object is already present!!!");
        try {
            Assertions.assertTrue(dao.save(author));
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(dao.findAll().contains(author));
        Assertions.assertTrue(dao.delete(author), "Failed to delete saved object");
    }

    @Test
    @DisplayName("Duplicate authors")
    public void saveDuplicate(){
        Assertions.assertTrue(dao.findByKey(author.getId()).isEmpty(), "Object is already present!!!");
        try {
            Assertions.assertTrue(dao.save(author));
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DuplicateObjectException.class,()->dao.save(author));
        Assertions.assertTrue(dao.delete(author), "Failed to delete saved object!");
    }

    @Test
    @DisplayName("Delete existing author")
    public void deleteAuthor(){
        if(dao.findByKey(author.getId()).isEmpty()){
            try {
                Assertions.assertTrue(dao.save(author));
            } catch (DuplicateObjectException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertFalse(dao.findByKey(author.getId()).isEmpty(), "Object has not found!!!");
        Assertions.assertTrue(dao.delete(author));
    }

    @Test
    @DisplayName("Delete non-existing author")
    public void deleteWrongAuthor(){
        Author wrongAuthor = new Author(-1, Date.valueOf("1991-01-01"), "WRONG", "WRONG",  'U', "WRONG", "WRONG", "WRONG");
        Assertions.assertFalse(dao.delete(wrongAuthor));
    }

    @Test
    @DisplayName("Update existing author")
    public void updateAuthor(){
        if(dao.findByKey(author.getId()).isEmpty()){
            try {
                Assertions.assertTrue(dao.save(author));
            } catch (DuplicateObjectException e) {
                throw new RuntimeException(e);
            }
        }

        Author newAuthor = dao.findByKey(999).orElse(null);
        Assertions.assertNotNull(newAuthor);

        newAuthor.setFirstname("UPDATED NAME");

        Assertions.assertTrue(dao.update(newAuthor));


        newAuthor = dao.findByKey(999).orElse(null);
        Assertions.assertNotNull(newAuthor);
        Assertions.assertEquals("UPDATED NAME", newAuthor.getFirstname());
        Assertions.assertTrue(dao.delete(newAuthor), "Failed to delete saved object!!!");
    }

    @Test
    @DisplayName("Fill table from CSV file")
    public void fillFromCsvTest() throws SQLException, DuplicateObjectException, IOException, CsvException {
        String src = "src/main/resources/csv/authors.csv";
        List<Author> oldAuthors = dao.findAll();
        List<Author> authors = dao.fillFromCsvFile(src);
        oldAuthors.addAll(authors);

        Assertions.assertFalse(authors.isEmpty());
        Assertions.assertEquals(oldAuthors, dao.findAll());

        Assertions.assertTrue(dao.delete(dao.findByKey(3).orElse(null)));
        Assertions.assertTrue(dao.delete(dao.findByKey(4).orElse(null)));
    }

    @Test
    @DisplayName("Save to CSV file")
    public void toCsvTest() throws SQLException, IOException {
        File dir = new File(CSV_OUTPUT_PATH);

        Assertions.assertTrue(dir.isDirectory());
        File[] filesBefore = dir.listFiles();
        Assertions.assertNotNull(filesBefore);

        Assertions.assertTrue(dao.toCsv());
        Assertions.assertEquals(filesBefore.length+1, dir.listFiles().length);
    }
}
