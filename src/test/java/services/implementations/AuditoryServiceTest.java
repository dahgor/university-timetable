package services.implementations;

import dao.DaoException;
import dao.entities.Auditory;
import dao.implementations.AuditoryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditoryServiceTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";
    private static int INVALID_ID = -1;

    @Mock
    private AuditoryDao auditoryDao;

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new AuditoryService(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new AuditoryService(auditoryDao).save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new AuditoryService(auditoryDao).findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToDeleteByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new AuditoryService(auditoryDao).deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnCorrectModelWhenSaved() throws ServiceException, DaoException {
        InOrder calls = inOrder(auditoryDao);
        when(auditoryDao.save(any())).thenReturn(1);
        AuditoryService auditoryService = new AuditoryService(auditoryDao);
        Auditory auditory = new Auditory(0, "1st floor");

        Auditory result = auditoryService.save(auditory);

        verify(auditoryDao).save(any());
        assertEquals(1, result.getId());
        assertEquals(auditory.getLocation(), result.getLocation());
    }

    @Test
    void shouldDeleteItemWhenIdPassed() throws ServiceException, DaoException {
        InOrder calls = Mockito.inOrder(auditoryDao);
        AuditoryService auditoryService = new AuditoryService(auditoryDao);
        Auditory auditory = new Auditory(1, "1st floor");

        auditoryService.deleteById(auditory.getId());

        calls.verify(auditoryDao).deleteById(auditory.getId());
    }

    @Test
    void shouldReturnCorrectModelWhenIdIsPassed() throws DaoException, ServiceException {
        Auditory auditoryEntity = new Auditory(1, "1st floor");
        InOrder calls = inOrder(auditoryDao);
        when(auditoryDao.findById(1)).thenReturn(auditoryEntity);
        AuditoryService auditoryService = new AuditoryService(auditoryDao);

        Auditory result = auditoryService.findById(auditoryEntity.getId());

        calls.verify(auditoryDao).findById(1);
        assertEquals(auditoryEntity.getId(), result.getId());
        assertEquals(auditoryEntity.getLocation(), result.getLocation());
    }

    @Test
    void shouldReturnListOfModels() throws DaoException, ServiceException {
        Auditory auditoryEntity = new Auditory(1, "1st floor");
        List<Auditory> entities = new LinkedList<>();
        entities.add(auditoryEntity);
        when(auditoryDao.findAllRecords()).thenReturn(entities);
        InOrder calls = inOrder(auditoryDao);
        AuditoryService auditoryService = new AuditoryService(auditoryDao);

        List<Auditory> result = auditoryService.getAllItems();

        verify(auditoryDao).findAllRecords();
        assertEquals(1, result.size());
        assertEquals(auditoryEntity.getId(), result.get(0).getId());
        assertEquals(auditoryEntity.getLocation(), result.get(0).getLocation());
    }

}