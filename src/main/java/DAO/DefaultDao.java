package DAO;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import exceptions.DuplicateObjectException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static DAO.Constants.CSV_OUTPUT_PATH;

public interface DefaultDao<T, K> {
    List<T> findAll() throws SQLException;

    Optional<T> findByKey(K key) throws SQLException;

    Boolean save(T obj) throws DuplicateObjectException, SQLException;

    Boolean update(T Obj);

    Boolean delete(T obj);

    T convertObject(ResultSet resultSet) throws SQLException;

    T convertObject(String[] objectLine);

    String[] getHeaderLine();

    String[] convertObjectForCsvExport(T object);

    default List<T> fillFromCsvFile(String path) throws IOException, CsvException, DuplicateObjectException, SQLException {
        List<T> objects = readFromCsvFile(path);

        if (!objects.isEmpty()) {
            for (T object : objects) {
                save(object);
            }
        }

        return objects;
    }

    default List<T> readFromCsvFile(String path) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(path));

        List<String[]> lines = reader.readAll();
        reader.close();

        lines.remove(0); //first line is columns titles line

        List<T> objects = new ArrayList<>();

        for (String[] line : lines) {
            T object = convertObject(line);
            objects.add(object);
        }

        if (objects.isEmpty()) {
            return Collections.emptyList();
        }
        return objects;
    }

    default Boolean toCsv() throws IOException, SQLException {
        List<T> objects = findAll();

        if (!objects.isEmpty()) {
            return writeToCsv(objects);
        }

        return false;
    }

    default Boolean writeToCsv(List<T> objects) throws IOException {
        if (!objects.isEmpty()) {
            String className = objects.get(0).getClass().getSimpleName().toLowerCase();
            String fileName = "/" + className + "s_" + System.currentTimeMillis() + ".csv";
            CSVWriter writer = new CSVWriter(new FileWriter(CSV_OUTPUT_PATH + fileName), ',', Character.MIN_VALUE, Character.MIN_VALUE, "\n");

            List<String[]> lines = convertObjects(objects);

            if (!lines.isEmpty()) {
                writer.writeNext(getHeaderLine());
                writer.writeAll(lines);
                writer.flush();
                writer.close();
                return true;
            }
        }
        return false;
    }

    default List<String[]> convertObjects(List<T> objects) {
        List<String[]> lines = new ArrayList<>();
        for (T object : objects) {
            lines.add(convertObjectForCsvExport(object));
        }
        return lines;
    }
}