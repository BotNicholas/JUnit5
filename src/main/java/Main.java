import DAO.AuthorDao;
import DAO.BookCategoryDao;
import DAO.BookDao;
import com.opencsv.exceptions.CsvException;
import connection.ConnectionManager;
import exceptions.DuplicateObjectException;

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

            ConnectionManager.closeConnection();
        } catch (IOException | DuplicateObjectException | CsvException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
