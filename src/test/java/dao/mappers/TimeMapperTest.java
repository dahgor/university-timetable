package dao.mappers;

import dao.entities.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeMapperTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTablesWithoutRelations.sql";

    private JdbcTemplate jdbcTemplate;
    private final Time time = new Time(1, Date.valueOf(LocalDate.now()), 1);

    @BeforeEach
    void prepareJdbcAndDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    void saveTime(Time time) {
        jdbcTemplate.update("insert into times(date, time_period_id) values (?,?)",
                time.getDate(), time.getTimePeriodId());
    }

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        saveTime(time);
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from times");

        Time result = null;
        if (resultSet.next()) {
            result = new TimeMapper().mapRow(resultSet, 1);
        }

        assertEquals(time, result);

        resultSet.close();
        statement.close();
        connection.close();
    }

}