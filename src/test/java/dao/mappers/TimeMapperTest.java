package dao.mappers;

import dao.entities.Time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeMapperTest {
    private static final int TIME_ID = 1;
    private static final int TIME_PERIOD_ID = 2;
    private static final Date DATE = Date.valueOf(LocalDate.now());

    @Mock
    private ResultSet resultSet;
    private TimeMapper timeMapper = new TimeMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("time_id")).thenReturn(TIME_ID);
        when(resultSet.getDate("date")).thenReturn(DATE);
        when(resultSet.getInt("time_period_id")).thenReturn(TIME_PERIOD_ID);

        Time result = timeMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("time_id");
        verify(resultSet).getDate("date");
        verify(resultSet).getInt("time_period_id");
        assertEquals(TIME_ID, result.getId());
        assertEquals(TIME_PERIOD_ID, result.getTimePeriodId());
        assertEquals(DATE, result.getDate());
    }

}