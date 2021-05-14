package services.implementations;

import dao.DaoException;
import dao.entities.TimePeriod;
import dao.interfaces.TimePeriodDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.TimePeriodService;

import java.sql.Timestamp;
import java.util.List;

@Component("timePeriodService")
public class TimePeriodServiceImpl implements TimePeriodService {
    private static final String DAO_ERROR = "Dao error";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";

    private static final Logger logger = LoggerFactory.getLogger(TimePeriodServiceImpl.class);

    private TimePeriodDao timePeriodDao;

    @Autowired
    public TimePeriodServiceImpl(TimePeriodDao timePeriodDao) throws ServiceException {
        if (timePeriodDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.timePeriodDao = timePeriodDao;
    }

    @Override
    public TimePeriod save(TimePeriod item) throws ServiceException {
        logger.info("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = timePeriodDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            return new TimePeriod(generatedId, item.getStartHour(), item.getEndHour());
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(TimePeriod item) throws ServiceException {
        logger.info("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            timePeriodDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public TimePeriod findById(int id) throws ServiceException {
        logger.info("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return timePeriodDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<TimePeriod> getAllItems() throws ServiceException {
        logger.info("Retrieving all items");
        try {
            return timePeriodDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items", e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeStartHour(TimePeriod timePeriod, Timestamp newTime) throws ServiceException {
        logger.info("Changing time period's start hour, time period = {}, new time = {}",
                timePeriod, newTime);
        if (timePeriod == null || newTime == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            timePeriodDao.changeStartHour(timePeriod.getId(), newTime);
        } catch (DaoException e) {
            logger.warn("Failed to change time period's start hour, time period = {}, new time = {}",
                    timePeriod, newTime);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeEndHour(TimePeriod timePeriod, Timestamp newTime) throws ServiceException {
        logger.info("Changing time period's end hour, time period = {}, new time = {}",
                timePeriod, newTime);
        if (timePeriod == null || newTime == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            timePeriodDao.changeEndHour(timePeriod.getId(), newTime);
        } catch (DaoException e) {
            logger.warn("Failed to change time period's end hour, time period = {}, new time = {}",
                    timePeriod, newTime);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
