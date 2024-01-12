package DAO;

import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
import objects.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BookDAOTest {
    static BookDao dao;
    static AuthorDao authorDAO;
    static BookCategoryDao categoryDAO;
    static Book book;

    @BeforeAll
    public static void setup(){
        try {
            ConnectionManager.buildConnection();
            dao = new BookDao();
            authorDAO = new AuthorDao();
            categoryDAO = new BookCategoryDao();
            book = new Book(999, authorDAO.findByKey(1).orElse(null), categoryDAO.findByKey(1).orElse(null), "TEST COMMENTS", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 999.99, "TEST TITLE");

        } catch (IOException | SQLException e) {
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
    @DisplayName("Find all books")
    public void findAllAuthors(){
        List<Book> books = dao.findAll();
        Assertions.assertNotNull(books);
        Assertions.assertFalse(books.isEmpty(), "Books list is empty");
    }

    @Test
    @DisplayName("Find one existing book")
    public void findValidBook(){
        Optional<Book> bookOptional = dao.findByKey(1);
        Assertions.assertTrue(bookOptional.isPresent());
        Assertions.assertDoesNotThrow(bookOptional::get, "Object should be obtained!");
    }

    @ParameterizedTest
    @ValueSource(ints = {999999, -1, 0})
    @DisplayName("Find non-existing books")
    public void findNonExistingBooks(Integer key){
        Optional<Book> bookOptional = dao.findByKey(key);
        Assertions.assertFalse(bookOptional.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, bookOptional::get, "NoSuchElementException should be thrown!");
    }

    @Test
    @DisplayName("Save new book")
    public void saveBook(){
        Assertions.assertTrue(dao.findByKey(book.getId()).isEmpty(), "Object is already present!!!");
        try {
            Assertions.assertTrue(dao.save(book));
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(dao.findAll().contains(book));
        Assertions.assertTrue(dao.delete(book), "Failed to delete saved object");
    }

    @Test
    @DisplayName("Duplicate books")
    public void saveDuplicate(){
        Assertions.assertTrue(dao.findByKey(book.getId()).isEmpty(), "Object is already present!!!");
        try {
            Assertions.assertTrue(dao.save(book));
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DuplicateObjectException.class,()->dao.save(book));
        Assertions.assertTrue(dao.delete(book), "Failed to delete saved object!");
    }

    @Test
    @DisplayName("Delete existing book")
    public void deleteBook(){
        if(dao.findByKey(book.getId()).isEmpty()){
            try {
                Assertions.assertTrue(dao.save(book), "Failed to save the book");
            } catch (DuplicateObjectException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertFalse(dao.findByKey(book.getId()).isEmpty(), "Object has not found!!!");
        Assertions.assertTrue(dao.delete(book));
    }

    @Test
    @DisplayName("Delete non-existing book")
    public void deleteWrongBook(){
        Book wrongBook = new Book(-1, AuthorDAOTest.author, BookCategoryDAOTest.category, "WRONG", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 999.99, "WRONG");
        Assertions.assertFalse(dao.delete(wrongBook));
    }

    @Test
    @DisplayName("Update existing book")
    public void updateBook(){
        if(dao.findByKey(book.getId()).isEmpty()){
            try {
                Assertions.assertTrue(dao.save(book), "Failed to save test book");
            } catch (DuplicateObjectException e) {
                throw new RuntimeException(e);
            }
        }

        Book newBook = dao.findByKey(999).orElse(null);
        Assertions.assertNotNull(newBook);

        newBook.setTitle("UPDATED TITLE");

        Assertions.assertTrue(dao.update(newBook), "Failed to update the book");


        newBook = dao.findByKey(999).orElse(null);
        Assertions.assertNotNull(newBook);
        Assertions.assertEquals("UPDATED TITLE", newBook.getTitle());
        Assertions.assertTrue(dao.delete(newBook), "Failed to delete saved object!!!");
    }
}
