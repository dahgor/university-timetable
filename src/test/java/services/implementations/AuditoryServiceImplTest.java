package services.implementations;

import dao.DaoException;
import dao.entities.Auditory;
import dao.implementations.AuditoryDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.AuditoryService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditoryServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";
    private static int INVALID_ID = -1;

    @Mock
    private AuditoryDaoImpl auditoryDao;

    private AuditoryService auditoryService;

    @BeforeEach
    void prepareAuditoryService() throws ServiceException {
        auditoryService = new AuditoryServiceImpl(auditoryDao);
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new AuditoryServiceImpl(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> auditoryService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> auditoryService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToDeleteByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> auditoryService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToChangeLocationMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> auditoryService.changeLocation(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnCorrectModelWhenSaved() throws ServiceException, DaoException {
        Auditory auditory = new Auditory(0, "1st floor");
        when(auditoryDao.save(any())).thenReturn(1);

        Auditory result = auditoryService.save(auditory);

        verify(auditoryDao).save(auditory);
        assertEquals(1, result.getId());
        assertEquals(auditory.getLocation(), result.getLocation());
    }

    @Test
    void shouldDeleteItemWhenIdPassed() throws ServiceException, DaoException {
        Auditory auditory = new Auditory(1, "1st floor");

        auditoryService.delete(auditory);

        verify(auditoryDao).deleteById(auditory.getId());
    }

    @Test
    void shouldReturnItemWhenIdIsPassed() throws DaoException, ServiceException {
        Auditory auditory = new Auditory(1, "1st floor");
        when(auditoryDao.findById(1)).thenReturn(auditory);

        Auditory result = auditoryService.findById(auditory.getId());

        verify(auditoryDao).findById(1);
        assertEquals(auditory.getId(), result.getId());
        assertEquals(auditory.getLocation(), result.getLocation());
    }

    @Test
    void shouldReturnListOfModels() throws DaoException, ServiceException {
        List<Auditory> auditories = new LinkedList<>();
        auditories.add(new Auditory(1, "1st floor"));
        auditories.add(new Auditory(2, "2nd floor"));
        when(auditoryDao.findAllRecords()).thenReturn(auditories);

        List<Auditory> result = auditoryService.getAllItems();

        verify(auditoryDao).findAllRecords();
        assertEquals(auditories, result);
    }

    @Test
    void shouldChangeLocationWhenValidArgsArePassed() throws ServiceException, DaoException {
        Auditory auditory = new Auditory(1, "1st floor");
        String newLocation = "3rd floor";

        auditoryService.changeLocation(auditory, newLocation);

        verify(auditoryDao).changeLocation(auditory.getId(), newLocation);
    }

}