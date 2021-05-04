package services.interfaces;

import dao.entities.TimePeriod;
import services.ServiceException;

import java.sql.Timestamp;

public interface TimePeriodService extends Service<TimePeriod> {

    void changeStartHour(TimePeriod timePeriod, Timestamp newTime) throws ServiceException;

    void changeEndHour(TimePeriod timePeriod, Timestamp newTime) throws ServiceException;
}
