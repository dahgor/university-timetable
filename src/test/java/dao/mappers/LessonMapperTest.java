package dao.mappers;

import dao.entities.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonMapperTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTablesWithoutRelations.sql";

    private JdbcTemplate jdbcTemplate;
    private final Lesson lesson = new Lesson(1, 1, 1, 1, 1);

    @BeforeEach
    void prepareJdbcAndDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    void saveLesson(Lesson lesson) {
        jdbcTemplate.update("insert into lessons(course_id, professor_id, group_id, auditory_id) " +
                        "values (?,?,?,?)", lesson.getCourseId(), lesson.getProfessorId(), lesson.getGroupId(),
                lesson.getAuditoryId());
    }

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        saveLesson(lesson);
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from lessons");

        Lesson result = null;
        if (resultSet.next()) {
            result = new LessonMapper().mapRow(resultSet, 1);
        }

        assertEquals(lesson, result);

        resultSet.close();
        statement.close();
        connection.close();
    }

}