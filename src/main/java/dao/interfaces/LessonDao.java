package dao.interfaces;

import dao.DaoException;
import dao.entities.Lesson;

import java.sql.Date;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    void changeAuditory(int lessonId, int auditoryId) throws DaoException;

    void changeCourse(int lessonId, int courseId) throws DaoException;

    void changeGroup(int lessonId, int groupId) throws DaoException;

    void changeProfessor(int lessonId, int professorId) throws DaoException;

    void changeTime(int lessonId, int timeId) throws DaoException;

    void assignLessonToTime(int lessonId, int timeId) throws DaoException;

    List<Lesson> findByCourse(int courseId) throws DaoException;

    List<Lesson> findByProfessor(int professorId) throws DaoException;

    List<Lesson> findByGroup(int groupId) throws DaoException;

    List<Lesson> findByAuditory(int auditoryId) throws DaoException;

    List<Lesson> findByTime(int timeId) throws DaoException;

    List<Lesson> findByGroupAndDate(int groupId, Date day) throws DaoException;

    List<Lesson> findByProfessorAndDate(int professorId, Date day) throws DaoException;

    List<Lesson> findByGroupAndMonth(int groupId, Date month) throws DaoException;

    List<Lesson> findByProfessorAndMonth(int professorId, Date month) throws DaoException;
}
