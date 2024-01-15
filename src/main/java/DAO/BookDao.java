package DAO;

import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
import objects.Author;
import objects.Book;
import objects.BookCategory;
import validating.BookValidator;
import validating.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.util.*;

import static DAO.Constants.SQL_PATH;

public class BookDao implements DefaultDao<Book, Integer> {
    private final Properties QUERIES;
    private static final String TABLE = "books";
    private final AuthorDao authorDAO;
    private final BookCategoryDao categoryDAO;

    public BookDao() throws IOException {
        QUERIES = new Properties();
        QUERIES.load(new FileInputStream(SQL_PATH));

        authorDAO = new AuthorDao();
        categoryDAO = new BookCategoryDao();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        try (Statement statement = ConnectionManager.getStatement()) {
            ResultSet resultSet = statement.executeQuery(QUERIES.getProperty("selectAll.query").replaceAll("TABLE_NAME", TABLE));
            while (resultSet.next()) {
                Book book = convertObject(resultSet);
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (books.isEmpty()) {
            return Collections.emptyList();
        }
        return books;
    }

    @Override
    public Optional<Book> findByKey(Integer key) {
        return findAll().stream().filter(book -> book.getId().equals(key)).findAny();
    }

    @Override
    public Boolean save(Book obj) throws DuplicateObjectException {
        if (findByKey(obj.getId()).isEmpty()) {
            try {
                ConnectionManager.updateIncrementForTable(TABLE);
                PreparedStatement statement = ConnectionManager.prepareStatement(QUERIES.getProperty("books.insert.query"), obj.getId(),
                                                                                                                            obj.getAuthor().getId(),
                                                                                                                            obj.getCategory().getCode(),
                                                                                                                            obj.getIsbn(),
                                                                                                                            obj.getPublicationDate(),
                                                                                                                            obj.getDateAcquired(),
                                                                                                                            obj.getTitle(),
                                                                                                                            obj.getRecommendedPrice(),
                                                                                                                            obj.getComments());
                Validator<Book> validator = new BookValidator();
                if (validator.isValid(obj)) {
                    return statement.executeUpdate() > 0;
                }
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new DuplicateObjectException();
        }
    }

    @Override
    public Boolean update(Book obj) {
        try (PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("books.update.query"), obj.getAuthor().getId(),
                                                                                                                                 obj.getCategory().getCode(),
                                                                                                                                 obj.getIsbn(),
                                                                                                                                 obj.getPublicationDate(),
                                                                                                                                 obj.getDateAcquired(),
                                                                                                                                 obj.getTitle(),
                                                                                                                                 obj.getRecommendedPrice(),
                                                                                                                                 obj.getComments(),
                                                                                                                                 obj.getId())) {
            Validator<Book> validator = new BookValidator();
            if (validator.isValid(obj)) {
                return preparedStatement.executeUpdate() > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(Book obj) {
        try (PreparedStatement statement = ConnectionManager.prepareStatement(QUERIES.getProperty("deleteByID.query").replaceAll("TABLE_NAME", TABLE), obj.getId())) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book convertObject(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        Author author = authorDAO.findByKey(resultSet.getInt("author_id")).orElse(null);
        BookCategory category = categoryDAO.findByKey(resultSet.getInt("book_category_code")).orElse(null);
        String comments = resultSet.getString("comments");
        Date acquiringDate = resultSet.getDate("date_aquired");
        String isbn = resultSet.getString("isbn");
        Date publicationDate = resultSet.getDate("publication_date");
        Double recommendedPrice = resultSet.getDouble("recommended_price");
        String title = resultSet.getString("title");

        return new Book(id, author, category, comments, acquiringDate, isbn, publicationDate, recommendedPrice, title);
    }
}