package dao.mappers;

import dao.entities.TimePeriod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimePeriodMapperTest {
    private static final int TIME_PERIOD_ID = 1;
    private static final Timestamp START_HOUR = Timestamp.valueOf(LocalDateTime.now());
    private static final Timestamp END_HOUR = Timestamp.valueOf(LocalDateTime.now());

    @Mock
    private ResultSet resultSet;
    private TimePeriodMapper timePeriodMapper = new TimePeriodMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("time_period_id")).thenReturn(TIME_PERIOD_ID);
        when(resultSet.getTimestamp("start_hour")).thenReturn(START_HOUR);
        when(resultSet.getTimestamp("end_hour")).thenReturn(END_HOUR);

        TimePeriod result = timePeriodMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("time_period_id");
        verify(resultSet).getTimestamp("start_hour");
        verify(resultSet).getTimestamp("end_hour");
        assertEquals(TIME_PERIOD_ID, result.getId());
        assertEquals(START_HOUR, result.getStartHour());
        assertEquals(END_HOUR, result.getEndHour());
    }

}