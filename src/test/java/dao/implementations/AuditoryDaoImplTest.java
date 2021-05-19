package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Auditory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditoryDaoImplTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/auditoryDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;

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

    private void saveAuditory(Auditory auditory) {
        jdbcTemplate.update("insert into auditories(auditory_location) VALUES (?)",
                auditory.getLocation());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new AuditoryDaoImpl(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> auditoryDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> auditoryDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> auditoryDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToChangeLocationMethod() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> auditoryDao.changeLocation(INVALID_ID, ""));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToChangeLocationMethod() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> auditoryDao.changeLocation(1, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameAuditoryFromDbWhenSaved() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);
        Auditory auditory = new Auditory(1, "1st floor");
        auditoryDao.save(auditory);

        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from auditories");

        assertTrue(result.next());
        assertEquals(auditory.getId(), result.getInt("auditory_id"));
        assertEquals(auditory.getLocation(), result.getString("auditory_location"));
    }

    @Test
    void shouldReturnCorrectIdWhenSaved() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);
        Auditory auditory1 = new Auditory(1, "1st floor");
        Auditory auditory2 = new Auditory(2, "Next to WC on the 3rd floor");

        int generatedId1 = auditoryDao.save(auditory1);
        int generatedId2 = auditoryDao.save(auditory2);

        assertEquals(1, generatedId1);
        assertEquals(2, generatedId2);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);
        Auditory auditory = new Auditory(1, "1st floor");
        saveAuditory(auditory);

        auditoryDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from auditories");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);
        Auditory auditory1 = new Auditory(1, "1st floor");
        Auditory auditory2 = new Auditory(2, "Next to WC on the 3rd floor");
        saveAuditory(auditory1);
        saveAuditory(auditory2);

        List<Auditory> itemsFromDb = auditoryDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(auditory1, itemsFromDb.get(0));
        assertEquals(auditory2, itemsFromDb.get(1));
    }

    @Test
    void shouldChangeLocationWhenValidIdIsPassed() throws DaoException {
        AuditoryDaoImpl auditoryDao = new AuditoryDaoImpl(jdbcTemplate, daoProperties);
        Auditory auditory = new Auditory(1, "1st floor");
        saveAuditory(auditory);
        String newLocation = "2nd floor";

        auditoryDao.changeLocation(1, newLocation);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from auditories");

        assertTrue(result.next());
        assertEquals(newLocation, result.getString("auditory_location"));
    }

}