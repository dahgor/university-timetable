package dao.interfaces;

import dao.DaoException;
import dao.entities.Lesson;

import java.sql.Date;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {
    List<Lesson> findScheduledLessonsForTime(int timeId) throws DaoException;

    void assignLessonToTime(int lessonId, int timeId) throws DaoException;

    List<Lesson> findLessonsWithCourse(int courseId) throws DaoException;

    List<Lesson> findLessonsWithProfessor(int professorId) throws DaoException;

    List<Lesson> findLessonsWithGroup(int groupId) throws DaoException;

    List<Lesson> findLessonsWithAuditory(int auditoryId) throws DaoException;

    List<Lesson> findLessonsForGroupForDay(int groupId, Date day) throws DaoException;

    List<Lesson> findLessonsForGroupForMonth(int groupId, Date month) throws DaoException;

    List<Lesson> findLessonsForProfessorForDay(int professorId, Date day) throws DaoException;

    List<Lesson> findLessonsForProfessorForMonth(int professorId, Date month) throws DaoException;
}
