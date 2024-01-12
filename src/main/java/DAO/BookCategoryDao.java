package DAO;

import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
import objects.BookCategory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static DAO.Constants.SQL_PATH;


public class BookCategoryDao implements DefaultDao<BookCategory, Integer> {

    private final Properties QUERIES;
    private static final String TABLE = "book_categories";

    public BookCategoryDao() throws IOException {
        QUERIES = new Properties();
        QUERIES.load(new FileInputStream(SQL_PATH));
    }

    @Override
    public List<BookCategory> findAll() {
        List<BookCategory> categories = new ArrayList<>();

        try (Statement statement = ConnectionManager.getStatement()) {
            ResultSet resultSet = statement.executeQuery(QUERIES.getProperty("selectAll.query").replaceAll("TABLE_NAME", TABLE));
            while (resultSet.next()) {
                BookCategory category = convertObject(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories;
    }

    @Override
    public Optional<BookCategory> findByKey(Integer key) {
        return findAll().stream().filter(category -> category.getCode().equals(key)).findAny();
    }

    @Override
    public Boolean save(BookCategory obj) throws DuplicateObjectException {
        if (findByKey(obj.getCode()).isEmpty()) {
            try {
                ConnectionManager.updateIncrementForTable(TABLE);
                PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("categories.insert.query"), obj.getCode(),
                                                                                                                                         obj.getDescription());
                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new DuplicateObjectException();
        }
    }

    @Override
    public Boolean update(BookCategory obj) {
        try (PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("categories.update.query"), obj.getDescription(),
                                                                                                                                      obj.getCode())) {
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(BookCategory obj) {
        try (PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("deleteByCode.query").replaceAll("TABLE_NAME", TABLE), obj.getCode())) {
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookCategory convertObject(ResultSet resultSet) throws SQLException {
        int code = resultSet.getInt("code");
        String categoryDescription = resultSet.getString("category_description");

        return new BookCategory(code, categoryDescription);
    }
}