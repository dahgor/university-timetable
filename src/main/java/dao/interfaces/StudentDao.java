package dao.interfaces;

import dao.DaoException;
import dao.entities.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    void changeGroup(int studentId, int groupId) throws DaoException;

    void changeFirstName(int studentId, String newFirstName) throws DaoException;

    void changeLastName(int studentId, String newLastName) throws DaoException;

    List<Student> findByGroup(int groupId) throws DaoException;
}
