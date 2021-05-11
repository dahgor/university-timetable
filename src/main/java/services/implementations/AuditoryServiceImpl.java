package services.implementations;

import dao.DaoException;
import dao.entities.Auditory;
import dao.implementations.AuditoryDaoImpl;
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
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = auditoryDao.save(item);
            return new Auditory(generatedId, item.getLocation());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public void delete(Auditory item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            auditoryDao.deleteById(item.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public Auditory findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return auditoryDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public List<Auditory> getAllItems() throws ServiceException {
        try {
            return auditoryDao.findAllRecords();
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public void changeLocation(Auditory auditory, String newLocation) throws ServiceException {
        if (auditory == null || newLocation == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            auditoryDao.changeLocation(auditory.getId(), newLocation);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}