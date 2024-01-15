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
import java.text.SimpleDateFormat;
import java.util.*;

import static DAO.Constants.SQL_PATH;

public class BookDao implements DefaultDao<Book, Integer> {
    private final Properties QUERIES;
    private static final String TABLE = "books";
    private final AuthorDao authorDAO;
    private final BookCategoryDao categoryDAO;

    private final Validator<Book> validator;

    public BookDao() throws IOException {
        QUERIES = new Properties();
        QUERIES.load(new FileInputStream(SQL_PATH));

        authorDAO = new AuthorDao();
        categoryDAO = new BookCategoryDao();

        validator = new BookValidator();
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
        if (obj != null) {
            try (PreparedStatement statement = ConnectionManager.prepareStatement(QUERIES.getProperty("deleteByID.query").replaceAll("TABLE_NAME", TABLE), obj.getId())) {
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
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

    @Override
    public Book convertObject(String[] objectLine) {
        int id = Integer.parseInt(objectLine[0]);
        Author author = authorDAO.findByKey(Integer.parseInt(objectLine[1])).orElse(null);
        BookCategory category = categoryDAO.findByKey(Integer.parseInt(objectLine[2])).orElse(null);
        String isbn = objectLine[3];
        Date publicationDate = Date.valueOf(objectLine[4]);
        Date acquiringDate = Date.valueOf(objectLine[5]);
        String title = objectLine[6];
        Double recommendedPrice = Double.parseDouble(objectLine[7]);
        String comments = objectLine[8];

        return new Book(id, author, category, comments, acquiringDate, isbn, publicationDate, recommendedPrice, title);
    }

    @Override
    public String[] convertObjectForCsvExport(Book object) {
        String pattern = "yyyy-M-d";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        String[] line = {
                object.getId().toString(),
                object.getAuthor().getId().toString(),
                object.getCategory().getCode().toString(),
                object.getIsbn(),
                dateFormat.format(object.getPublicationDate()),
                dateFormat.format(object.getDateAcquired()),
                object.getTitle(),
                object.getRecommendedPrice().toString(),
                object.getComments()
        };

        return line;
    }

    public String[] getHeaderLine() {
        return new String[]{"id", "author_id", "book_category_code", "isbn", "publication_date", "date_aquired", "title", "recommended_price", "comments"};
    }
}