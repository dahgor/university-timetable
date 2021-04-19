package dao.mappers;

import dao.entities.Lesson;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LessonMapper implements RowMapper<Lesson> {
    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(rs.getInt("lesson_id"));
        lesson.setCourseId(rs.getInt("course_id"));
        lesson.setProfessorId(rs.getInt("professor_id"));
        lesson.setGroupId(rs.getInt("group_id"));
        lesson.setAuditoryId(rs.getInt("auditory_id"));
        return lesson;
    }
}
