package dao.interfaces;

import dao.DaoException;
import dao.entities.Course;

import java.util.List;

public interface CourseDao extends Dao<Course> {

    void changeName(int courseId, String newName) throws DaoException;

    void changeDescription(int courseId, String newDescription) throws DaoException;

    List<Course> findByProfessor(int professorId) throws DaoException;

    List<Course> findByGroup(int groupId) throws DaoException;
}
