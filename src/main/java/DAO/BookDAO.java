package DAO;

import connection.DBConnection;
import exceptions.DuplicateObjectException;
import objects.Author;
import objects.Book;
import objects.BookCategory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BookDAO implements DefaultDAO<Book, Integer>{
    private final Connection CONNECTION;
    private final Properties PROPERTIES;
    private static final String SQL_PATH="src/main/resources/queries.properties";
    private static final String TABLE = "books";

    private final AuthorDAO authorDAO;
    private final BookCategoryDAO categoryDAO;

    public BookDAO() throws SQLException, IOException {
        CONNECTION = DBConnection.buildConnection();
        PROPERTIES = new Properties();
        PROPERTIES.load(new FileInputStream(SQL_PATH));
        authorDAO = new AuthorDAO();
        categoryDAO = new BookCategoryDAO();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try(Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PROPERTIES.getProperty("selectAll.query").replaceAll("TABLE_NAME", TABLE));

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                Author author = authorDAO.findByKey(resultSet.getInt("author_id")).orElse(null);
                BookCategory category = categoryDAO.findByKey(resultSet.getInt("book_category_code")).orElse(null);
                String comments = resultSet.getString("comments");
                Date acquiringDate = resultSet.getDate("date_aquired");
                String isbn = resultSet.getString("isbn");
                Date publicationDate = resultSet.getDate("publication_date");
                Double recommendedPrice = resultSet.getDouble("recommended_price");
                String title = resultSet.getString("title");

                Book book = new Book(id, author, category, comments, acquiringDate, isbn, publicationDate, recommendedPrice, title);

                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(books.isEmpty())
            return Collections.emptyList();

        return books;
    }

    @Override
    public Optional<Book> findByKey(Integer key) {
        Optional<Book> book = Optional.empty();
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("selectByKey.query")
                                                                                .replaceAll("TABLE_NAME", TABLE))){
            statement.setInt(1, key);

            ResultSet result = statement.executeQuery();
            if(result.next()){
                int id = result.getInt("id");
                Author author = authorDAO.findByKey(result.getInt("author_id")).orElse(null);
                BookCategory category = categoryDAO.findByKey(result.getInt("book_category_code")).orElse(null);
                String comments = result.getString("comments");
                Date acquiringDate = result.getDate("date_aquired");
                String isbn = result.getString("isbn");
                Date publicationDate = result.getDate("publication_date");
                Double recommendedPrice = result.getDouble("recommended_price");
                String title = result.getString("title");

                book = Optional.of(new Book(id, author, category, comments, acquiringDate, isbn, publicationDate, recommendedPrice, title));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public Boolean save(Book obj) throws DuplicateObjectException {
        if(findByKey(obj.getId()).isEmpty()) {
            if (updateIncrement()) {
                try (PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("books.insert.query"))) {
                    statement.setInt(1, obj.getId());
                    statement.setInt(2, obj.getAuthor().getId());
                    statement.setInt(3, obj.getCategory().getCode());
                    statement.setString(4, obj.getIsbn());
                    statement.setDate(5, obj.getPublicationDate());
                    statement.setDate(6, obj.getDateAcquired());
                    statement.setString(7, obj.getTitle());
                    statement.setDouble(8, obj.getRecommendedPrice());
                    statement.setString(9, obj.getComments());

                    if (statement.executeUpdate() > 0) {
                        return true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new DuplicateObjectException();
        }
        return false;
    }

    @Override
    public Boolean update(Book obj) {
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("books.update.query"))){
            statement.setInt(1, obj.getAuthor().getId());
            statement.setInt(2, obj.getCategory().getCode());
            statement.setString(3, obj.getIsbn());
            statement.setDate(4, obj.getPublicationDate());
            statement.setDate(5, obj.getDateAcquired());
            statement.setString(6, obj.getTitle());
            statement.setDouble(7, obj.getRecommendedPrice());
            statement.setString(8, obj.getComments());
            statement.setInt(9, obj.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Boolean delete(Book obj) {
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("deleteByID.query").replaceAll("TABLE_NAME", TABLE))) {
            statement.setInt(1, obj.getId());

            if(statement.executeUpdate()>0){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Boolean updateIncrement() {
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("increment.update.query").replaceAll("TABLE_NAME", TABLE))){
            if(statement.executeUpdate()>=0) {
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
