package services.implementations;

import dao.DaoException;
import dao.entities.Auditory;
import dao.implementations.AuditoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.Service;
import services.models.AuditoryModel;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditoryService implements Service<AuditoryModel> {
    private static final String DAO_ERROR = "Dao error";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";

    private AuditoryDao auditoryDao;

    public AuditoryService() {
    }

    @Autowired
    public AuditoryService(AuditoryDao auditoryDao) throws ServiceException {
        if (auditoryDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.auditoryDao = auditoryDao;
    }

    @Override
    public AuditoryModel save(AuditoryModel item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        Auditory auditoryEntity = new Auditory();
        auditoryEntity.setLocation(item.getLocation());
        try {
            int generatedId = auditoryDao.save(auditoryEntity);
            return new AuditoryModel(generatedId, item.getLocation());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            auditoryDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public AuditoryModel findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            Auditory auditoryEntity = auditoryDao.findById(id);
            return new AuditoryModel(auditoryEntity.getId(), auditoryEntity.getLocation());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }

    @Override
    public List<AuditoryModel> getAllItems() throws ServiceException {
        try {
            List<Auditory> entities = auditoryDao.findAllRecords();
            return entities.stream().map(entity -> {
                AuditoryModel model = new AuditoryModel();
                model.setId(entity.getId());
                model.setLocation(entity.getLocation());
                return model;
            }).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR);
        }
    }
}
