package dao.mappers;

import dao.entities.Auditory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditoryMapper implements RowMapper<Auditory> {
    @Override
    public Auditory mapRow(ResultSet rs, int rowNum) throws SQLException {
        Auditory auditory = new Auditory();
        auditory.setId(rs.getInt("auditory_id"));
        auditory.setLocation(rs.getString("auditory_location"));
        return auditory;
    }
}
