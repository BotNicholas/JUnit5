package DAO;

import connection.ConnectionManager;
import exceptions.DuplicateObjectException;
import objects.Author;
import validating.AuthorValidator;
import validating.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static DAO.Constants.SQL_PATH;

public class AuthorDao implements DefaultDao<Author, Integer> {

    private final Properties QUERIES;
    private static final String TABLE = "authors";
    private static Validator<Author> validator;

    public AuthorDao() throws IOException {
        QUERIES = new Properties();
        QUERIES.load(new FileInputStream(SQL_PATH));

        validator = new AuthorValidator();
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();

        try (Statement statement = ConnectionManager.getStatement()) {
            ResultSet resultSet = statement.executeQuery(QUERIES.getProperty("selectAll.query").replaceAll("TABLE_NAME", TABLE));
            while (resultSet.next()) {
                Author author = convertObject(resultSet);
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (authors.isEmpty()) {
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public Optional<Author> findByKey(Integer key) {
        return findAll().stream().filter(author -> author.getId().equals(key)).findAny();
    }

    @Override
    public Boolean save(Author obj) throws DuplicateObjectException {
        if (findByKey(obj.getId()).isEmpty()) {
            try {
                ConnectionManager.updateIncrementForTable(TABLE);
                PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("authors.insert.query"), obj.getId(),
                                                                                                                                      obj.getFirstname(),
                                                                                                                                      obj.getLastname(),
                                                                                                                                      obj.getInitials(),
                                                                                                                                      obj.getBirthDate(),
                                                                                                                                      obj.getGender().toString(),
                                                                                                                                      obj.getContactDetails(),
                                                                                                                                      obj.getOtherDetails());
                if (validator.isValid(obj)) {
                    return preparedStatement.executeUpdate() > 0;
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
    public Boolean update(Author obj) {
        try (PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("authors.update.query"), obj.getFirstname(),
                                                                                                                                   obj.getLastname(),
                                                                                                                                   obj.getInitials(),
                                                                                                                                   obj.getBirthDate(),
                                                                                                                                   obj.getGender().toString(),
                                                                                                                                   obj.getContactDetails(),
                                                                                                                                   obj.getOtherDetails(),
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
    public Boolean delete(Author obj) {
        if (obj != null) {
            try (PreparedStatement preparedStatement = ConnectionManager.prepareStatement(QUERIES.getProperty("deleteByID.query").replaceAll("TABLE_NAME", TABLE), obj.getId())) {
                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public Author convertObject(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        Date birthDate = resultSet.getDate("birth_date");
        String contactDetails = resultSet.getString("contact_details");
        String firstname = resultSet.getString("firstname");
        char gender = resultSet.getString("gender").charAt(0);
        String initials = resultSet.getString("initials");
        String lastname = resultSet.getString("lastname");
        String otherDetails = resultSet.getString("other_details");

        return new Author(id, birthDate, contactDetails, firstname, gender, initials, lastname, otherDetails);
    }

    @Override
    public Author convertObject(String[] objectLine) {
        int id = Integer.parseInt(objectLine[0]);
        String firstname = objectLine[1];
        String lastname = objectLine[2];
        String initials = objectLine[3];
        Date birthDate = Date.valueOf(objectLine[4]);
        char gender = objectLine[5].charAt(0);
        String contactDetails = objectLine[6];
        String otherDetails = objectLine[7];

        return new Author(id, birthDate, contactDetails, firstname, gender, initials, lastname, otherDetails);
    }

    public String[] convertObjectForCsvExport(Author object) {
        String pattern = "yyyy-M-d";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        String[] line = {
                object.getId().toString(),
                object.getFirstname(),
                object.getLastname(),
                object.getInitials(),
                dateFormat.format(object.getBirthDate()),
                object.getGender().toString(),
                object.getContactDetails(),
                object.getOtherDetails()
        };

        return line;
    }

    public String[] getHeaderLine() {
        return new String[]{"id", "firstname", "lastname", "initials", "birth_date", "gender", "contact_details", "other_details"};
    }
}