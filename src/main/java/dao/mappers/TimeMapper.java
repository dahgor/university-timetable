package dao.mappers;

import dao.entities.Time;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeMapper implements RowMapper<Time> {
    @Override
    public Time mapRow(ResultSet rs, int rowNum) throws SQLException {
        Time time = new Time();
        time.setId(rs.getInt("time_id"));
        time.setDate(rs.getDate("date"));
        time.setTimePeriodId(rs.getInt("time_period_id"));
        return time;
    }
}
