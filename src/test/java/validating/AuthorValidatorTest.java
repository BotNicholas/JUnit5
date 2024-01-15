package validating;

import objects.Author;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.text.ParseException;

public class AuthorValidatorTest {
    public static Author validAuthor;
    public static Author invalidAuthor;
    public static AuthorValidator validator;

    @BeforeAll
    public static void setUp() {
        validAuthor = new Author(1, Date.valueOf("1900-01-01"), "CONTACT DETAILS", "VALID", 'M', "VA", "AUTHOR", "OTHER_DETAILS");
        invalidAuthor = new Author(-2, new Date(new java.util.Date().getTime()), "CONTACT DETAILS", "INVALID", 'M', "VA", "AUTHOR", "OTHER_DETAILS");

        validator = new AuthorValidator();
    }
    @BeforeEach
    public void beforeEach(TestInfo info){
        System.out.println("Executing test: " + info.getDisplayName());
    }

    @Test
    @DisplayName("Testing valid author")
    public void testValidAuthor(){
        System.out.println("\tChecking valid author...");
        Assertions.assertTrue(validator.isValid(validAuthor), "This author is valid!");
        System.out.println("\tGood!");
    }

    @Test
    @DisplayName("Testing invalid author")
    public void testInvalidAuthor(){
        System.out.println("\tChecking invalid author...");
        Assertions.assertFalse(validator.isValid(invalidAuthor), "This author is invalid!");
        System.out.println("\tGood!");
        System.out.println("\tChecking NULL author...");
        Assertions.assertFalse(validator.isValid(null));
        System.out.println("\tGood!");
    }
}
