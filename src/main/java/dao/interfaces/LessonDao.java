package dao.interfaces;

import dao.DaoException;
import dao.entities.Lesson;

import java.util.List;

public interface LessonDao extends Dao<Lesson> {
    List<Lesson> findScheduledLessonsForTime(int timeId) throws DaoException;

    void assignLessonToTime(int lessonId, int timeId) throws DaoException;
}
