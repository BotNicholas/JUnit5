package validating;

import objects.BookCategory;
import org.junit.jupiter.api.*;

public class BookCategoryValidatorTest {
    public static BookCategory validBookCategory;
    public static BookCategory invalidBookCategory;
    public static BookCategoryValidator validator;

    @BeforeAll
    public static void setUp() {
        validBookCategory = new BookCategory(1, "TEST DESCRIPTION");
        invalidBookCategory = new BookCategory(-1, "TEST DESCRIPTION");

        validator = new BookCategoryValidator();
    }
    @BeforeEach
    public void beforeEach(TestInfo info){
        System.out.println("Executing test: " + info.getDisplayName());
    }

    @Test
    @DisplayName("Testing valid book category")
    public void testValidCategory(){
        System.out.println("\tChecking valid category...");
        Assertions.assertTrue(validator.isValid(validBookCategory), "This category is valid!");
        System.out.println("\tGood!");
    }

    @Test
    @DisplayName("Testing invalid book category")
    public void testInvalidCategory(){
        System.out.println("\tChecking invalid category...");
        Assertions.assertFalse(validator.isValid(invalidBookCategory), "This category is invalid!");
        System.out.println("\tGood!");
        System.out.println("\tChecking NULL category...");
        Assertions.assertFalse(validator.isValid(null));
        System.out.println("\tGood!");
    }
}
