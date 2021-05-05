package services.implementations;

import dao.DaoException;
import dao.entities.Time;
import dao.entities.TimePeriod;
import dao.interfaces.TimeDao;
import dao.interfaces.TimePeriodDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.TimeService;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";
    private static final int INVALID_ID = -1;
    private static final Date DATE = Date.valueOf(LocalDate.now());
    private static final int TIME_PERIOD = 1;

    @Mock
    private TimeDao timeDao;
    @Mock
    private TimePeriodDao timePeriodDao;

    private TimeService timeService;

    @BeforeEach
    void prepareTimeService() throws ServiceException {
        timeService = new TimeServiceImpl(timeDao, timePeriodDao);
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new TimeServiceImpl(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timeService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timeService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToDeleteByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timeService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowServiceExceptionWhenNullIsPassedToGetTimePeriodMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> timeService.getTimePeriod(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnCorrectItemWhenSaved() throws ServiceException, DaoException {
        Time time = new Time(DATE, TIME_PERIOD);
        when(timeDao.save(any())).thenReturn(1);

        Time result = timeService.save(time);

        verify(timeDao).save(time);
        assertEquals(1, result.getId());
        assertEquals(time.getDate(), result.getDate());
        assertEquals(time.getTimePeriodId(), result.getTimePeriodId());
    }

    @Test
    void shouldDeleteItemWhenIdPassed() throws ServiceException, DaoException {
        Time time = new Time(1, DATE, TIME_PERIOD);

        timeService.delete(time);

        verify(timeDao).deleteById(time.getId());
    }

    @Test
    void shouldReturnItemWhenIdIsPassed() throws DaoException, ServiceException {
        Time time = new Time(1, DATE, TIME_PERIOD);
        when(timeDao.findById(time.getId())).thenReturn(time);

        Time result = timeService.findById(time.getId());

        verify(timeDao).findById(1);
        assertEquals(time.getId(), result.getId());
        assertEquals(time.getDate(), result.getDate());
        assertEquals(time.getTimePeriodId(), result.getTimePeriodId());
    }

    @Test
    void shouldReturnItems() throws DaoException, ServiceException {
        List<Time> times = new LinkedList<>();
        times.add(new Time(1, DATE, TIME_PERIOD));
        times.add(new Time(2, Date.valueOf(LocalDate.now()), 1));
        when(timeDao.findAllRecords()).thenReturn(times);

        List<Time> result = timeService.getAllItems();

        verify(timeDao).findAllRecords();
        assertEquals(times, result);
    }

    @Test
    void shouldReturnTimePeriod() throws DaoException, ServiceException {
        Time time = new Time(1, DATE, TIME_PERIOD);
        TimePeriod timePeriod = new TimePeriod(1, Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()));
        when(timePeriodDao.findById(time.getTimePeriodId())).thenReturn(timePeriod);

        TimePeriod result = timeService.getTimePeriod(time);

        verify(timePeriodDao).findById(time.getTimePeriodId());
        assertEquals(timePeriod, result);
    }

}