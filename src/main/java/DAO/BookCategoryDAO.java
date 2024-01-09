package DAO;

import connection.DBConnection;
import exceptions.DuplicateObjectException;
import objects.BookCategory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class BookCategoryDAO implements DefaultDAO<BookCategory, Integer>{
    private final Connection CONNECTION;
    private final Properties PROPERTIES;
    private static final String SQL_PATH="src/main/resources/queries.properties";
    private static final String TABLE = "book_categories";

    public BookCategoryDAO() throws SQLException, IOException {
        CONNECTION = DBConnection.buildConnection();
        PROPERTIES = new Properties();
        PROPERTIES.load(new FileInputStream(SQL_PATH));
    }

    @Override
    public List<BookCategory> findAll() {
        List<BookCategory> bookCategories = new ArrayList<>();
        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PROPERTIES.getProperty("selectAll.query").replaceAll("TABLE_NAME", TABLE));

            while(resultSet.next()){
                int code = resultSet.getInt("code");
                String categoryDescription = resultSet.getString("category_description");

                BookCategory category = new BookCategory(code, categoryDescription);

                bookCategories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(bookCategories.isEmpty())
            return Collections.emptyList();

        return bookCategories;
    }

    @Override
    public Optional<BookCategory> findByKey(Integer key) {
        Optional<BookCategory> category = Optional.empty();
        try (PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("selectByCode.query")
                                                                                .replaceAll("TABLE_NAME", TABLE))){
            statement.setInt(1, key);

            ResultSet result = statement.executeQuery();
            if(result.next()){
                int code = result.getInt("code");
                String categoryDescription = result.getString("category_description");

                category = Optional.of(new BookCategory(code, categoryDescription));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Boolean save(BookCategory obj) throws DuplicateObjectException {
        if(findByKey(obj.getCode()).isEmpty()) {
            if (updateIncrement()) {
                try (PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("categories.insert.query"))) {
                    statement.setInt(1, obj.getCode());
                    statement.setString(2, obj.getDescription());

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
    public Boolean update(BookCategory obj) {
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("categories.update.query"))){
            statement.setString(1, obj.getDescription());
            statement.setInt(2, obj.getCode());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Boolean delete(BookCategory obj) {
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("deleteByCode.query").replaceAll("TABLE_NAME", TABLE))) {
            statement.setInt(1, obj.getCode());

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
