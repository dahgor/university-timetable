package services.implementations;

import dao.DaoException;
import dao.entities.Time;
import dao.interfaces.TimeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.Service;

import java.util.List;

@Component("timeService")
public class TimeService implements Service<Time> {
    private static final String DAO_ERROR = "Dao error";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";

    private TimeDao timeDao;

    @Autowired
    public TimeService(TimeDao timeDao) throws ServiceException {
        if (timeDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.timeDao = timeDao;
    }

    @Override
    public Time save(Time item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = timeDao.save(item);
            return new Time(generatedId, item.getDate(), item.getTimePeriodId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Time item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            timeDao.deleteById(item.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Time findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return timeDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Time> getAllItems() throws ServiceException {
        try {
            return timeDao.findAllRecords();
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
