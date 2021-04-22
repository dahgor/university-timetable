package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.TimePeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimePeriodDaoTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/timePeriodDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";
    private static final int INVALID_ID = -1;
    private static final Timestamp START = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(10, 0)));
    private static final Timestamp END = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(12, 0)));
    private static final Timestamp START_2 = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(12, 0)));
    private static final Timestamp END_2 = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(14, 0)));

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

    void saveTimePeriod(TimePeriod timePeriod) {
        jdbcTemplate.update("insert into time_periods(start_hour, end_hour) values (?, ?)",
                timePeriod.getStartHour(), timePeriod.getEndHour());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new TimePeriodDao(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        TimePeriodDao timePeriodDao = new TimePeriodDao(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> timePeriodDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        TimePeriodDao timePeriodDao = new TimePeriodDao(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> timePeriodDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        TimePeriodDao timePeriodDao = new TimePeriodDao(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> timePeriodDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameAuditoryFromDbWhenSaved() throws DaoException {
        TimePeriodDao timePeriodDao = new TimePeriodDao(jdbcTemplate, daoProperties);
        TimePeriod timePeriod = new TimePeriod(1, START, END);
        timePeriodDao.save(timePeriod);

        TimePeriod result = timePeriodDao.findById(1);

        assertEquals(timePeriod, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        TimePeriodDao timePeriodDao = new TimePeriodDao(jdbcTemplate, daoProperties);
        TimePeriod timePeriod = new TimePeriod(1, START, END);
        saveTimePeriod(timePeriod);

        timePeriodDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from time_periods");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        TimePeriodDao timePeriodDao = new TimePeriodDao(jdbcTemplate, daoProperties);
        TimePeriod timePeriod1 = new TimePeriod(1, START, END);
        TimePeriod timePeriod2 = new TimePeriod(2, START_2, END_2);
        saveTimePeriod(timePeriod1);
        saveTimePeriod(timePeriod2);

        List<TimePeriod> itemsFromDb = timePeriodDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(timePeriod1, itemsFromDb.get(0));
        assertEquals(timePeriod2, itemsFromDb.get(1));
    }

}