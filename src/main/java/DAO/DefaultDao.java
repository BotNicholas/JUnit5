package DAO;

import exceptions.DuplicateObjectException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DefaultDao<T, K> {
    List<T> findAll() throws SQLException;

    Optional<T> findByKey(K key) throws SQLException;

    Boolean save(T obj) throws DuplicateObjectException, SQLException;

    Boolean update(T Obj);

    Boolean delete(T obj);

    T convertObject(ResultSet resultSet) throws SQLException;
}