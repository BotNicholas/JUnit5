import DAO.AuthorDAO;
import DAO.BookCategoryDAO;
import DAO.BookDAO;
import objects.Author;
import objects.BookCategory;
import services.BookService;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            AuthorDAO authorDAO = new AuthorDAO();
            BookCategoryDAO categoryDAO = new BookCategoryDAO();
            BookDAO bookDAO = new BookDAO();

            BookService service = new BookService(bookDAO);

            service.findAll().forEach(System.out::println);

            System.out.println("\n---               ---------------");
            service.findBooksCheaperThan(20.0).forEach(System.out::println);

            System.out.println("\n------------------");
            System.out.println(service.getTotalBooksPrice());

            System.out.println("\n------------------");
            service.findAllBooksForAuthor(authorDAO.findByKey(2).orElse(new Author())).forEach(System.out::println);

            System.out.println("\n------------------");
            System.out.println(service.countBooksByCategory(categoryDAO.findByKey(1).orElse(new BookCategory())));
//            System.out.println(service.countBooksByCategory(categoryDAO.findByKey(2).orElse(new BookCategory())));
//            System.out.println(service.countBooksByCategory(new BookCategory()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        try {
//
//            BookDAO dao = new BookDAO();
//            AuthorDAO authorDAO = new AuthorDAO();
//            BookCategoryDAO categoryDAO = new BookCategoryDAO();
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            Book book = new Book(999, authorDAO.findByKey(1).orElse(null), categoryDAO.findByKey(1).orElse(null), "TEST COMMENTS", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 999.99, "TEST TITLE");
////            Book book2 = new Book(998, authorDAO.findByKey(1).orElse(null), categoryDAO.findByKey(1).orElse(null), "TEST COMMENTS", Date.valueOf("1990-3-22"), "000-0-00-000000-0", Date.valueOf("1990-3-20"), 999.99, "TEST TITLE");
//            dao.save(book);
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            book.setTitle("UPDATED TITLE");
//            dao.update(book);
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//            dao.delete(book);
//            dao.findAll().forEach(System.out::println);
//            System.out.println();
//
//
//            System.out.println("\n-------------------------------");
//
//            categoryDAO.findAll().forEach(System.out::println);
//            System.out.println();
//
//            BookCategory category = new BookCategory(999, "TEST");
//            categoryDAO.save(category);
//            categoryDAO.findAll().forEach(System.out::println);
//            System.out.println();
//
//            category.setDescription("UPDATED CATEGORY");
//            categoryDAO.update(category);
//            categoryDAO.findAll().forEach(System.out::println);
//            System.out.println();
//
//            categoryDAO.delete(category);
//            categoryDAO.findAll().forEach(System.out::println);
//            System.out.println();
//
//
//
//
//
//            System.out.println("\n-------------------------------");
//
//            authorDAO.findAll().stream().forEach(System.out::println);
//            System.out.println();
////            Author author = new Author();
////            author.setId(999);
////            dao .delete(author);
//
//
//            Author author = new Author(999, Date.valueOf("1990-3-22"), "TEST CONTACT DETAILS", "TEST", 'M', "TA", "AUTHOR", "TEST OTHER DETAILS");
//            authorDAO.save(author);
//
//            authorDAO.findAll().stream().forEach(System.out::println);
//            System.out.println();
//
//            author.setFirstname("UPDATED TEST NAME");
//            authorDAO.update(author);
//
//            authorDAO.findAll().stream().forEach(System.out::println);
//            System.out.println();
//
//            authorDAO.delete(author);
//
//            authorDAO.findAll().stream().forEach(System.out::println);
//            System.out.println();
//
//
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (DuplicateObjectException e) {
//            throw new RuntimeException(e);
//        }
    }
}
