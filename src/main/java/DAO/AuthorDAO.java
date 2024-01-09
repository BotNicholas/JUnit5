package DAO;

import connection.DBConnection;
import exceptions.DuplicateObjectException;
import objects.Author;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;

import java.sql.*;
import java.util.*;

public class AuthorDAO implements DefaultDAO<Author, Integer>{
    private final Connection CONNECTION;
    private final Properties PROPERTIES;
    private static final String SQL_PATH = "src/main/resources/queries.properties";
    private static final String TABLE = "authors";

    public AuthorDAO() throws SQLException, IOException {
        CONNECTION = DBConnection.buildConnection();
        PROPERTIES = new Properties();
        PROPERTIES.load( new FileInputStream(SQL_PATH));
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PROPERTIES.getProperty("selectAll.query").replaceAll("TABLE_NAME", TABLE));

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                Date birthDate = resultSet.getDate("birth_date");
                String contactDetails = resultSet.getString("contact_details");
                String firstname = resultSet.getString("firstname");
                char gender = resultSet.getString("gender").charAt(0);
                String initials = resultSet.getString("initials");
                String lastname = resultSet.getString("lastname");
                String otherDetails = resultSet.getString("other_details");

                Author author = new Author(id, birthDate, contactDetails, firstname, gender, initials, lastname, otherDetails);

                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(authors.isEmpty())
            return Collections.emptyList();

        return authors;
    }

    @Override
    public Optional<Author> findByKey(Integer key) {
        Optional<Author> author = Optional.empty();
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("selectByKey.query")
                                                                                .replaceAll("TABLE_NAME", TABLE))){
            statement.setInt(1, key);

            ResultSet result = statement.executeQuery();
            if(result.next()){
                int id = result.getInt("id");
                Date birthDate = result.getDate("birth_date");
                String contactDetails = result.getString("contact_details");
                String firstname = result.getString("firstname");
                char gender = result.getString("gender").charAt(0);
                String initials = result.getString("initials");
                String lastname = result.getString("lastname");
                String otherDetails = result.getString("other_details");

                author = Optional.of(new Author(id, birthDate, contactDetails, firstname, gender, initials, lastname, otherDetails));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return author;
    }

    @Override
    public Boolean save(Author obj) throws DuplicateObjectException {
        if(findByKey(obj.getId()).isEmpty()) {
            if (updateIncrement()) {
                try (PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("authors.insert.query"))) {
                    statement.setInt(1, obj.getId());
                    statement.setString(2, obj.getFirstname());
                    statement.setString(3, obj.getLastname());
                    statement.setString(4, obj.getInitials());
                    statement.setDate(5, obj.getBirthDate());
                    statement.setString(6, obj.getGender().toString());
                    statement.setString(7, obj.getContactDetails());
                    statement.setString(8, obj.getOtherDetails());

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
    public Boolean update(Author obj) {
        try(PreparedStatement statement = CONNECTION.prepareStatement(PROPERTIES.getProperty("authors.update.query"))){
            statement.setString(1, obj.getFirstname());
            statement.setString(2, obj.getLastname());
            statement.setString(3, obj.getInitials());
            statement.setDate(4, obj.getBirthDate());
            statement.setString(5, obj.getGender().toString());
            statement.setString(6, obj.getContactDetails());
            statement.setString(7, obj.getOtherDetails());
            statement.setInt(8, obj.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Boolean delete(Author obj) {
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
