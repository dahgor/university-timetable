package dao.mappers;

import dao.entities.Student;
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

class StudentMapperTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTablesWithoutRelations.sql";

    private JdbcTemplate jdbcTemplate;
    private final Student student = new Student(1, 1, "David", "Smith");

    @BeforeEach
    void prepareJdbcAndDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    void saveStudent(Student student) {
        jdbcTemplate.update("insert into students(group_id, first_name, last_name) values (?,?,?)",
                student.getGroupId(), student.getFirstName(), student.getLastName());
    }

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        saveStudent(student);
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from students");

        Student result = null;
        if (resultSet.next()) {
            result = new StudentMapper().mapRow(resultSet, 1);
        }

        assertEquals(student, result);

        resultSet.close();
        statement.close();
        connection.close();
    }

}