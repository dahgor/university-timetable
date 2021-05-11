package services.interfaces;

import dao.entities.Time;
import dao.entities.TimePeriod;
import services.ServiceException;

public interface TimeService extends Service<Time> {

    TimePeriod getTimePeriod(Time time) throws ServiceException;
}
