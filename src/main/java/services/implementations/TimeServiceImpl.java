package services.implementations;

import dao.DaoException;
import dao.entities.Time;
import dao.entities.TimePeriod;
import dao.interfaces.TimeDao;
import dao.interfaces.TimePeriodDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.TimeService;

import java.util.List;

@Component("timeService")
public class TimeServiceImpl implements TimeService {
    private static final String DAO_ERROR = "Dao error";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";

    private static final Logger logger = LoggerFactory.getLogger(TimeServiceImpl.class);

    private TimeDao timeDao;
    private TimePeriodDao timePeriodDao;

    public TimeServiceImpl() {
    }

    @Autowired
    public TimeServiceImpl(TimeDao timeDao, TimePeriodDao timePeriodDao) throws ServiceException {
        if (timeDao == null || timePeriodDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.timeDao = timeDao;
        this.timePeriodDao = timePeriodDao;
    }

    @Override
    public Time save(Time item) throws ServiceException {
        logger.info("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = timeDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            return new Time(generatedId, item.getDate(), item.getTimePeriodId());
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Time item) throws ServiceException {
        logger.info("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            timeDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Time findById(int id) throws ServiceException {
        logger.info("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return timeDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Time> getAllItems() throws ServiceException {
        logger.info("Retrieving all items");
        try {
            return timeDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items", e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public TimePeriod getTimePeriod(Time time) throws ServiceException {
        logger.info("Retrieving time period of time, time = {}", time);
        if (time == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return timePeriodDao.findById(time.getTimePeriodId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve time period of time, time = {}", time, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
