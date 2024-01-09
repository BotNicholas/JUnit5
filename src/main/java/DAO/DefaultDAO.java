package DAO;

import connection.DBConnection;
import exceptions.DuplicateObjectException;

import java.util.List;
import java.util.Optional;

public interface DefaultDAO <T, K> {
    List<T> findAll();
    Optional<T> findByKey(K key);
    Boolean save(T obj) throws DuplicateObjectException;
//    Boolean update(T newObj, K key);
    Boolean update(T Obj);
    Boolean delete(T obj);

    Boolean updateIncrement();
}

//ToDo: Implement Simple dao
