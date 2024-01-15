import DAO.AuthorDao;
import DAO.BookCategoryDao;
import DAO.BookDao;
import com.opencsv.exceptions.CsvException;
import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
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

            authorDao.findAll().forEach(System.out::println);
            System.out.println("\n----------------");

            authorDao.fillFromCsvFile("src/main/resources/csv/authors.csv").forEach(System.out::println);
            System.out.println("\n----------------");

            authorDao.findAll().forEach(System.out::println);
            System.out.println("\n----------------");

            authorDao.toCsv();



            BookCategoryDao categoryDao = new BookCategoryDao();

            categoryDao.findAll().forEach(System.out::println);
            System.out.println("\n----------------");

            categoryDao.fillFromCsvFile("src/main/resources/csv/categories.csv").forEach(System.out::println);
            System.out.println("\n----------------");

            categoryDao.findAll().forEach(System.out::println);
            System.out.println("\n----------------");

            categoryDao.toCsv();


            BookDao bookDao = new BookDao();

            bookDao.findAll().forEach(System.out::println);
            System.out.println("\n----------------");

            bookDao.fillFromCsvFile("src/main/resources/csv/books.csv").forEach(System.out::println);
            System.out.println("\n----------------");

            bookDao.findAll().forEach(System.out::println);
            System.out.println("\n----------------");

            bookDao.toCsv();


            authorDao.delete(authorDao.findByKey(3).orElse(null));
            authorDao.delete(authorDao.findByKey(4).orElse(null));

            categoryDao.delete(categoryDao.findByKey(3).orElse(null));
            categoryDao.delete(categoryDao.findByKey(4).orElse(null));



//            AuthorDao authorDao = new AuthorDao();
//            BookCategoryDao categoryDao = new BookCategoryDao();
//
//
//            categoryDao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            categoryDao.fillFromCsvFile("src/main/resources/csv/categories.csv");
//
//            categoryDao.findAll().forEach(System.out::println);
//            System.out.println();
//
//
//
//            authorDao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            authorDao.fillFromCsvFile("src/main/resources/csv/authors.csv");
//
//            authorDao.findAll().forEach(System.out::println);
//            System.out.println();
//
//
//
//            BookDao dao = new BookDao();
//
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            dao.fillFromCsvFile("src/main/resources/csv/books.csv").forEach(System.out::println);
//            System.out.println();
//
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//
//            authorDao.delete(authorDao.findByKey(3).orElse(null));
//            authorDao.delete(authorDao.findByKey(4).orElse(null));
//            categoryDao.delete(categoryDao.findByKey(3).orElse(null));
//            categoryDao.delete(categoryDao.findByKey(4).orElse(null));






//            BookCategoryDao dao = new BookCategoryDao();
//
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            dao.fillFromCsvFile("src/main/resources/csv/categories.csv");
//
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            dao.delete(dao.findByKey(3).orElse(null));
//            dao.delete(dao.findByKey(4).orElse(null));



//            AuthorDao dao = new AuthorDao();
//
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            dao.fillFromCsvFile("src/main/resources/csv/authors.csv");
//
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            dao.delete(dao.findByKey(3).orElse(null));
//            dao.delete(dao.findByKey(4).orElse(null));



//            AuthorDao authorDao = new AuthorDao();
//            BookCategoryDao categoryDao = new BookCategoryDao();
//            BookDao bookDao = new BookDao();
//
//            BookService service = new BookService(bookDao);
//
//            service.findAll().forEach(System.out::println);
//
//            System.out.println("\n------------------");
//            service.findBooksCheaperThan(20.0).forEach(System.out::println);
//
//            System.out.println("\n------------------");
//            System.out.println(service.getTotalBooksPrice());
//
//            System.out.println("\n------------------");
//            service.findAllBooksForAuthor(authorDao.findByKey(2).orElse(new Author())).forEach(System.out::println);
//
//            System.out.println("\n------------------");
//            System.out.println(service.countBooksByCategory(categoryDao.findByKey(1).orElse(new BookCategory())));
//
//            System.out.println(bookDao.findByKey(1));
//            System.out.println(service.countBooksByCategory(categoryDao.findByKey(2).orElse(new BookCategory())));
//            System.out.println(service.countBooksByCategory(new BookCategory()));
//
//            System.out.println(service.getSortedBooksByRank());

            ConnectionManager.closeConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
//        } catch (DuplicateObjectException e) {
//            throw new RuntimeException(e);
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
    }
}
