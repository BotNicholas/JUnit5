package services;

import DAO.BookDao;
import objects.Author;
import objects.Book;
import objects.BookCategory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    //Мы не делаем эти объекты статическими, так как хотим, чтобы для каждого теста создавался новый, собственный экземпляр объекта + MockitoAnnotations.openMocks(this); - использует this!!!
    /*@Mock
    static AuthorDAO dao;*/
    @Mock
    BookDao bookDAO;
    @InjectMocks
    BookService service;
    List<Book> books;
    Author author1;
    Author author2;
    BookCategory category1;
    BookCategory category2;

    @BeforeEach
    public void init(TestInfo testInfo){
        //replaced by @InjectMocks
//        MockitoAnnotations.openMocks(this); //will make new mock object
//        service = new BookService(bookDAO);

        author1 = new Author(999, Date.valueOf("1990-3-22"), "TEST CONTACT DETAILS", "TEST1", 'M', "TA1", "AUTHOR1", "TEST OTHER DETAILS");
        author2 = new Author(989, Date.valueOf("1990-3-22"), "TEST CONTACT DETAILS", "TEST2", 'F', "TA2", "AUTHOR2", "TEST OTHER DETAILS");

        category1 = new BookCategory(999, "TEST CATEGORY");
        category2 = new BookCategory(998, "TEST CATEGORY2");

        books =  new ArrayList<>();
        books.add(new Book(1, author2, category1, "TEST COMMENTS", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 19.99, "TEST TITLE"));
        books.add(new Book(2, author1, category2, "TEST COMMENTS", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 29.99, "TEST TITLE"));
        books.add(new Book(3, author2, category1, "TEST COMMENTS", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 999.99, "TEST TITLE"));

        System.out.println(testInfo.getDisplayName()+": ");
    }

    @Test
    @DisplayName("Find all books")
    public void findAllTest(){
        Mockito.when(bookDAO.findAll()).thenReturn(books);

        int expected = 3;

        List<Book> result = service.findAll();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(expected, result.size());
        Assertions.assertSame(books, result);
        System.out.printf("\t-> Expected %d and actual list size is %d\n", expected, result.size());



//        Mockito.verify(bookDAO, Mockito.times(1)).findAll();
//        Mockito.verify(bookDAO, Mockito.times(2)).findAll();
//
//        List<Book> spy = Mockito.spy(books);
//
//        Mockito.doReturn(1000).when(spy).size();
//
//        System.out.println(spy.size());
    }

    @ParameterizedTest(name = "executing test with [{arguments}]")
    @MethodSource("argumentsProviderForCheaperThanTest")
    @DisplayName("Find books cheaper than")
    public void findBooksCheaperThanTest(double price, int expected){
        Mockito.when(bookDAO.findAll()).thenReturn(books);

        int actual = service.findBooksCheaperThan(price).size();

        Assertions.assertEquals(expected, actual);
        System.out.printf("\t-> Expected books count for price %.2f is %d - actual is %d\n", price, expected, actual);
    }

    public static Stream<Arguments> argumentsProviderForCheaperThanTest(){
        return Stream.of(Arguments.of(20.0, 1),
                         Arguments.of(100.0, 2),
                         Arguments.of(29.99, 1),
                         Arguments.of(100.99, 2),
                         Arguments.of(1000.0, 3),
                         Arguments.of(0.0, 0));
    }



    @Test
    @DisplayName("Find total books price (sum)")
    public void findTotalPrice(){
        Mockito.when(bookDAO.findAll()).thenReturn(books);

        Double expected = 1049.97;
        Double actual = service.getTotalBooksPrice();

        Assertions.assertEquals(expected, actual);
        System.out.printf("\t-> Expected sum's %.2f and actual sum's %.2f\n", expected, actual);
    }

    @Test
    @DisplayName("Find all books for authors")
    public void findAllBooksForAuthorTest(){
        Mockito.when(bookDAO.findAll()).thenReturn(books);

        int expected = 1;
        int actual = service.findAllBooksForAuthor(author1).size();
        Assertions.assertEquals(expected, actual);
        System.out.printf("\t-> Expected books for %s author - %d, actual is %d\n", author1.getFirstname(), expected, actual);


        expected = 2;
        actual = service.findAllBooksForAuthor(author2).size();
        Assertions.assertEquals(expected, actual);
        System.out.printf("\t-> Expected books for %s author - %d, actual is %d\n", author2.getFirstname(), expected, actual);
    }

    @Test
    @DisplayName("Count books of category")
    public void countBooksByCategory(){
        Mockito.when(bookDAO.findAll()).thenReturn(books);

        int expected = 2;
        int actual = service.countBooksByCategory(category1);
        Assertions.assertEquals(expected, actual);
        System.out.printf("\t-> Expected books count of %s category - %d, actual is %d\n", category1.getDescription(), expected, actual);

        expected = 1;
        actual = service.countBooksByCategory(category2);
        Assertions.assertEquals(expected, actual);
        System.out.printf("\t-> Expected books count of %s category - %d, actual is %d\n", category2.getDescription(), expected, actual);
    }
}
