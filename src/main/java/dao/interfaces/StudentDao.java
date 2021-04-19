package dao.interfaces;

import dao.DaoException;
import dao.entities.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> findStudentsInGroup(int groupId) throws DaoException;
}
