package dao.interfaces;

import dao.DaoException;
import dao.entities.Time;

public interface TimeDao extends Dao<Time> {
    Time findScheduledTimeForLesson(int lessonId) throws DaoException;
}
