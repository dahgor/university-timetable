package services.interfaces;

import services.ServiceException;

import java.util.List;

public interface Service<T> {

    T save(T item) throws ServiceException;

    void deleteById(int id) throws ServiceException;

    T findById(int id) throws ServiceException;

    List<T> getAllItems() throws ServiceException;

}
