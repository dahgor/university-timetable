package dao.interfaces;

import dao.DaoException;
import dao.entities.TimePeriod;

import java.sql.Timestamp;

public interface TimePeriodDao extends Dao<TimePeriod> {

    void changeStartHour(int timePeriodId, Timestamp newTime) throws DaoException;

    void changeEndHour(int timePeriodId, Timestamp newTime) throws DaoException;

}
