package dao.interfaces;

import dao.DaoException;

import java.util.List;

public interface Dao<T> {

    void save(T item) throws DaoException;
    void deleteById(int id) throws DaoException;
    T findById(int id) throws DaoException;
    List<T> findAllRecords() throws DaoException;
}
