package DAO;

import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
import objects.BookCategory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BookCategoryDAOTest {
    static BookCategoryDao dao;
    static BookCategory category;

    @BeforeAll
    public static void setup(){
        try {
            ConnectionManager.buildConnection();
            dao = new BookCategoryDao();
            category = new BookCategory(999, "TEST CATEGORY");

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
    @DisplayName("Find all book categories")
    public void findAllCategories(){
        List<BookCategory> categories = dao.findAll();
        Assertions.assertNotNull(categories);
        Assertions.assertFalse(categories.isEmpty(), "Categories list is empty");
    }

    @Test
    @DisplayName("Find one existing category")
    public void findValidCategory(){
        Optional<BookCategory> categoryOptional = dao.findByKey(1);
        Assertions.assertTrue(categoryOptional.isPresent());
        Assertions.assertDoesNotThrow(categoryOptional::get, "Object should be obtained!");
    }

    @ParameterizedTest
    @ValueSource(ints = {999999, -1, 0})
    @DisplayName("Find non-existing categories")
    public void findNonExistingCategories(Integer key){
        Optional<BookCategory> categoryOptional = dao.findByKey(key);
        Assertions.assertFalse(categoryOptional.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, categoryOptional::get, "NoSuchElementException should be thrown!");
    }

    @Test
    @DisplayName("Save new category")
    public void saveCategory(){
        Assertions.assertTrue(dao.findByKey(category.getCode()).isEmpty(), "Object is already present!!!");
        try {
            Assertions.assertTrue(dao.save(category));
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(dao.findAll().contains(category));
        Assertions.assertTrue(dao.delete(category), "Failed to delete saved object!");
        System.out.println(dao.findAll());
    }

    @Test
    @DisplayName("Duplicate categories")
    public void saveDuplicate(){
        Assertions.assertTrue(dao.findByKey(category.getCode()).isEmpty(), "Object is already present!!!");
        try {
            Assertions.assertTrue(dao.save(category));
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DuplicateObjectException.class,()->dao.save(category));
        Assertions.assertTrue(dao.delete(category), "Failed to delete saved object!");
    }

    @Test
    @DisplayName("Delete existing category")
    public void deleteCategory(){
        if(dao.findByKey(category.getCode()).isEmpty()){
            try {
                Assertions.assertTrue(dao.save(category));
            } catch (DuplicateObjectException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertFalse(dao.findByKey(category.getCode()).isEmpty(), "Object has not found!!!");
        Assertions.assertTrue(dao.delete(category), "Failed to delete saved object!!!");
    }

    @Test
    @DisplayName("Delete non-existing category")
    public void deleteWrongAuthor(){
        BookCategory wrongCategory = new BookCategory(-1, "WRONG");
        Assertions.assertFalse(dao.delete(wrongCategory), "Failed to delete saved object!!!");
    }

    @Test
    @DisplayName("Update existing category")
    public void updateCategory(){
        if(dao.findByKey(category.getCode()).isEmpty()){
            try {
                Assertions.assertTrue(dao.save(category));
            } catch (DuplicateObjectException e) {
                throw new RuntimeException(e);
            }
        }

        BookCategory newCategory = dao.findByKey(999).orElse(null);
        Assertions.assertNotNull(newCategory);

        newCategory.setDescription("UPDATED DESCRIPTION");

        Assertions.assertTrue(dao.update(newCategory));


        newCategory = dao.findByKey(999).orElse(null);
        Assertions.assertNotNull(newCategory);
        Assertions.assertEquals("UPDATED DESCRIPTION", newCategory.getDescription());
        Assertions.assertTrue(dao.delete(newCategory), "Failed to delete the object!!!");
    }
}
