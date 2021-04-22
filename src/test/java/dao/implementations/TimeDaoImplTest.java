package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeDaoImplTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/timeDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;
    private static final int TIME_PERIOD_ID = 1;
    private static final int LESSON_ID = 1;
    private static final Date DATE = Date.valueOf(LocalDate.now());
    private static final Date DATE_2 = Date.valueOf(LocalDate.of(2021, 4, 15));
    private static final Timestamp TIMESTAMP = Timestamp.valueOf(LocalDateTime.now());

    private JdbcTemplate jdbcTemplate;
    private DaoProperties daoProperties;


    @BeforeEach
    void prepareJdbcAndProperties() throws FileNotFoundException, DaoException {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        FileInputStream file = new FileInputStream(PROPERTIES);
        daoProperties = new DaoProperties(file);
    }

    void saveTime(Time time) {
        jdbcTemplate.update("insert into times(date, time_period_id) values (?, ?)",
                time.getDate(), time.getTimePeriodId());
    }

    void createTimePeriod() {
        jdbcTemplate.update("insert into time_periods(start_hour, end_hour) " +
                "VALUES (?, ?)", TIMESTAMP, TIMESTAMP);
    }

    void createLesson() {
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-15')");
        jdbcTemplate.execute("insert into courses(course_name, course_description) " +
                "VALUES ('Math', 'description')");
        jdbcTemplate.execute("insert into professors(first_name, last_name) " +
                "VALUES ('Alan', 'Smith') ");
        jdbcTemplate.execute("insert into auditories(auditory_location) VALUES ('1st floor')");
        jdbcTemplate.execute("insert into lessons(course_id, professor_id, group_id, auditory_id) " +
                "VALUES (1, 1, 1, 1)");
    }

    void assignLessonToTime(int lessonId, int timeId) {
        jdbcTemplate.update("insert into schedule(time_id, lesson_id) VALUES (?, ?)",
                timeId, lessonId);
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new TimeDaoImpl(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> timeDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> timeDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> timeDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameTimeFromDbWhenSaved() throws DaoException {
        createTimePeriod();
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);
        Time time = new Time(1, DATE, TIME_PERIOD_ID);
        timeDao.save(time);

        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from times");

        assertTrue(result.next());
        assertEquals(time.getId(), result.getInt("time_id"));
        assertEquals(time.getDate(), result.getDate("date"));
        assertEquals(time.getTimePeriodId(), result.getInt("time_period_id"));
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        createTimePeriod();
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);
        Time time = new Time(1, DATE, TIME_PERIOD_ID);
        saveTime(time);

        timeDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from times");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        createTimePeriod();
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);
        Time time1 = new Time(1, DATE, TIME_PERIOD_ID);
        Time time2 = new Time(2, DATE_2, TIME_PERIOD_ID);
        saveTime(time1);
        saveTime(time2);

        List<Time> itemsFromDb = timeDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(time1, itemsFromDb.get(0));
        assertEquals(time2, itemsFromDb.get(1));
    }

    @Test
    void shouldRetrieveScheduledTimeForLessons() throws DaoException {
        createTimePeriod();
        createLesson();
        TimeDaoImpl timeDao = new TimeDaoImpl(jdbcTemplate, daoProperties);
        Time time = new Time(1, DATE, TIME_PERIOD_ID);
        saveTime(time);
        assignLessonToTime(LESSON_ID, time.getId());

        Time result = timeDao.findScheduledTimeForLesson(LESSON_ID);

        assertEquals(time, result);
    }

}