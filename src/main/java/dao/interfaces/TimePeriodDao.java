package dao.interfaces;

import dao.DaoException;

import java.sql.Timestamp;

public interface TimePeriodDao {

    void changeStartHour(int timePeriodId, Timestamp newTime) throws DaoException;

    void changeEndHour(int timePeriodId, Timestamp newTime) throws DaoException;

}
