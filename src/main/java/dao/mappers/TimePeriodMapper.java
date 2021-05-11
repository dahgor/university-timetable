package dao.mappers;

import dao.entities.TimePeriod;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimePeriodMapper implements RowMapper<TimePeriod> {
    @Override
    public TimePeriod mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setId(rs.getInt("time_period_id"));
        timePeriod.setStartHour(rs.getTimestamp("start_hour"));
        timePeriod.setEndHour(rs.getTimestamp("end_hour"));
        return timePeriod;
    }
}
