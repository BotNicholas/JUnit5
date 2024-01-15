package validating;

import objects.Author;
import objects.Book;
import objects.BookCategory;
import org.junit.jupiter.api.*;

import java.sql.Date;

public class BookValidatorTest {
    public static Book validBook;
    public static Book invalidBook;
    public static BookValidator validator;

    @BeforeAll
    public static void setUp() {
        Author validAuthor = new Author(1, Date.valueOf("1900-01-01"), "CONTACT DETAILS", "VALID", 'M', "VA", "AUTHOR", "OTHER_DETAILS");
        Author invalidAuthor = new Author(-2, new Date(new java.util.Date().getTime()), "CONTACT DETAILS", "INVALID", 'M', "VA", "AUTHOR", "OTHER_DETAILS");

        BookCategory validBookCategory = new BookCategory(1, "TEST DESCRIPTION");
        BookCategory invalidBookCategory = new BookCategory(-1, "TEST DESCRIPTION");

        java.util.Date now = new java.util.Date();

        validBook = new Book(1, validAuthor, validBookCategory, "COMMENTS", new Date(now.getTime()), "123-4-56-789098-7", new Date(now.getTime()), 500.0, "VALID TITLE");
        invalidBook = new Book(1, invalidAuthor, invalidBookCategory, "COMMENTS", new Date(now.getTime()+123456789L), "123-4-56-789098-7", new Date(now.getTime()+123456789L), 5000.0, "INVALID TITLE");

        validator = new BookValidator();
    }
    @BeforeEach
    public void beforeEach(TestInfo info){
        System.out.println("Executing test: " + info.getDisplayName());
    }

    @Test
    @DisplayName("Testing valid book")
    public void testValid(){
        System.out.println("\tChecking valid book...");
        Assertions.assertTrue(validator.isValid(validBook), "This category is valid!");
        System.out.println("\tGood!");
    }

    @Test
    @DisplayName("Testing invalid book")
    public void testInvalid(){
        System.out.println("\tChecking invalid book...");
        Assertions.assertFalse(validator.isValid(invalidBook), "This category is invalid!");
        System.out.println("\tGood!");
        System.out.println("\tChecking NULL book...");
        Assertions.assertFalse(validator.isValid(null));
        System.out.println("\tGood!");
    }
}
