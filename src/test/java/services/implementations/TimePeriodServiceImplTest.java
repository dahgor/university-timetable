package services.implementations;

import dao.DaoException;
import dao.entities.TimePeriod;
import dao.interfaces.TimePeriodDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.TimePeriodService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimePeriodServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";
    private static int INVALID_ID = -1;
    private static final Timestamp START_HOUR = Timestamp.
            valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
    private static final Timestamp END_HOUR = Timestamp.
            valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));

    @Mock
    private TimePeriodDao timePeriodDao;

    private TimePeriodService timePeriodService;

    @BeforeEach
    void prepareTimePeriodService() throws ServiceException {
        timePeriodService = new TimePeriodServiceImpl(timePeriodDao);
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new TimePeriodServiceImpl(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timePeriodService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timePeriodService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToDeleteByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timePeriodService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeStartHourMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timePeriodService.changeStartHour(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeEndHourMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timePeriodService.changeEndHour(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnCorrectModelWhenSaved() throws ServiceException, DaoException {
        TimePeriod timePeriod = new TimePeriod(START_HOUR, END_HOUR);
        when(timePeriodDao.save(timePeriod)).thenReturn(1);

        TimePeriod result = timePeriodService.save(timePeriod);

        verify(timePeriodDao).save(timePeriod);
        assertEquals(1, result.getId());
        assertEquals(timePeriod.getStartHour(), result.getStartHour());
        assertEquals(timePeriod.getEndHour(), result.getEndHour());
    }

    @Test
    void shouldDeleteItemWhenIdPassed() throws ServiceException, DaoException {
        TimePeriod timePeriod = new TimePeriod(1, START_HOUR, END_HOUR);

        timePeriodService.delete(timePeriod);

        verify(timePeriodDao).deleteById(timePeriod.getId());
    }

    @Test
    void shouldReturnItemWhenIdIsPassed() throws DaoException, ServiceException {
        TimePeriod timePeriod = new TimePeriod(1, START_HOUR, END_HOUR);
        when(timePeriodDao.findById(1)).thenReturn(timePeriod);

        TimePeriod result = timePeriodService.findById(timePeriod.getId());

        verify(timePeriodDao).findById(1);
        assertEquals(timePeriod, result);
    }

    @Test
    void shouldReturnAllItems() throws DaoException, ServiceException {
        List<TimePeriod> timePeriods = new LinkedList<>();
        timePeriods.add(new TimePeriod(1, START_HOUR, END_HOUR));
        when(timePeriodDao.findAllRecords()).thenReturn(timePeriods);

        List<TimePeriod> result = timePeriodService.getAllItems();

        verify(timePeriodDao).findAllRecords();
        assertEquals(timePeriods, result);
    }

    @Test
    void shouldChangeStartHourWhenValidArgsArePassed() throws ServiceException, DaoException {
        TimePeriod timePeriod = new TimePeriod(1, START_HOUR, END_HOUR);
        Timestamp newTime = END_HOUR;

        timePeriodService.changeStartHour(timePeriod, newTime);

        verify(timePeriodDao).changeStartHour(timePeriod.getId(), newTime);
    }


}