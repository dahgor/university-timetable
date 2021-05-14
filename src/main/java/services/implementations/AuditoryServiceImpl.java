package services.implementations;

import dao.DaoException;
import dao.entities.Auditory;
import dao.implementations.AuditoryDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.AuditoryService;

import java.util.List;

@Component
public class AuditoryServiceImpl implements AuditoryService {
    private static final String DAO_ERROR = "Dao error";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";

    private static final Logger logger = LoggerFactory.getLogger(AuditoryServiceImpl.class);

    private AuditoryDaoImpl auditoryDao;

    public AuditoryServiceImpl() {
    }

    @Autowired
    public AuditoryServiceImpl(AuditoryDaoImpl auditoryDao) throws ServiceException {
        if (auditoryDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.auditoryDao = auditoryDao;
    }

    @Override
    public Auditory save(Auditory item) throws ServiceException {
        logger.info("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = auditoryDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            return new Auditory(generatedId, item.getLocation());
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Auditory item) throws ServiceException {
        logger.info("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            auditoryDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Auditory findById(int id) throws ServiceException {
        logger.info("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return auditoryDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Auditory> getAllItems() throws ServiceException {
        logger.info("Retrieving all items");
        try {
            return auditoryDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items");
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeLocation(Auditory auditory, String newLocation) throws ServiceException {
        logger.info("Changing auditory's location, auditory = {}, new location = {}", auditory, newLocation);
        if (auditory == null || newLocation == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            auditoryDao.changeLocation(auditory.getId(), newLocation);
        } catch (DaoException e) {
            logger.warn("Failed to change location, item = {}, new location = {}", auditory, newLocation, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
