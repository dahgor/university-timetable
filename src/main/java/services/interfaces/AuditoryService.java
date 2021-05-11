package services.interfaces;

import dao.entities.Auditory;
import services.ServiceException;

public interface AuditoryService extends Service<Auditory> {

    void changeLocation(Auditory auditory, String newLocation) throws ServiceException;
}
