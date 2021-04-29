package dao.interfaces;

import dao.DaoException;
import dao.entities.Auditory;

public interface AuditoryDao extends Dao<Auditory> {

    void changeLocation(int auditoryId, String newLocation) throws DaoException;

}
