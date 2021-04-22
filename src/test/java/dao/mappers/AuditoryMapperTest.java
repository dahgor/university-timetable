package dao.mappers;

import dao.entities.Auditory;
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

class AuditoryMapperTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTablesWithoutRelations.sql";

    private JdbcTemplate jdbcTemplate;
    private final Auditory auditory = new Auditory(1, "1st floor");

    @BeforeEach
    void prepareJdbcAndDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    void saveAuditory(Auditory auditory) {
        jdbcTemplate.update("insert into auditories(auditory_location) values (?)",
                auditory.getLocation());
    }

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        saveAuditory(auditory);
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from auditories");

        Auditory result = null;
        if (resultSet.next()) {
            result = new AuditoryMapper().mapRow(resultSet, 1);
        }

        assertEquals(auditory, result);

        resultSet.close();
        statement.close();
        connection.close();
    }
}