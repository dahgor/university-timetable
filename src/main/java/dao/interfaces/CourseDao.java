package dao.interfaces;

import dao.DaoException;
import dao.entities.Course;

import java.util.List;

public interface CourseDao extends Dao<Course> {
    List<Course> findCoursesForProfessor(int professorId) throws DaoException;

    List<Course> findCoursesForGroup(int groupId) throws DaoException;
}
