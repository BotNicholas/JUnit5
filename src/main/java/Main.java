import DAO.AuthorDao;
import DAO.BookCategoryDao;
import DAO.BookDao;
import connection.ConnectionManager;
import objects.Author;
import objects.BookCategory;
import services.BookService;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            ConnectionManager.buildConnection();

            AuthorDao authorDao = new AuthorDao();
            BookCategoryDao categoryDao = new BookCategoryDao();
            BookDao bookDao = new BookDao();

            BookService service = new BookService(bookDao);

            service.findAll().forEach(System.out::println);

            System.out.println("\n------------------");
            service.findBooksCheaperThan(20.0).forEach(System.out::println);

            System.out.println("\n------------------");
            System.out.println(service.getTotalBooksPrice());

            System.out.println("\n------------------");
            service.findAllBooksForAuthor(authorDao.findByKey(2).orElse(new Author())).forEach(System.out::println);

            System.out.println("\n------------------");
            System.out.println(service.countBooksByCategory(categoryDao.findByKey(1).orElse(new BookCategory())));

            System.out.println(bookDao.findByKey(1));
            System.out.println(service.countBooksByCategory(categoryDao.findByKey(2).orElse(new BookCategory())));
            System.out.println(service.countBooksByCategory(new BookCategory()));

            ConnectionManager.closeConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
