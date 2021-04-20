package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Auditory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditoryDaoTest {
    public static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    public static final String AUDITORY_PROPERTIES =
            "./src/test/resources/daoProperties/auditoryDao.properties";
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final int INVALID_ID = -1;

    private JdbcTemplate jdbcTemplate;
    private DaoProperties daoProperties;

    @BeforeEach
    void prepareJdbcAndProperties() throws FileNotFoundException {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        FileInputStream file = new FileInputStream(AUDITORY_PROPERTIES);
        daoProperties = new DaoProperties(file);
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new AuditoryDao(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        AuditoryDao auditoryDao = new AuditoryDao(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> auditoryDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        AuditoryDao auditoryDao = new AuditoryDao(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> auditoryDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        AuditoryDao auditoryDao = new AuditoryDao(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> auditoryDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameAuditoryFromDbWhenSaved() throws DaoException {
        AuditoryDao auditoryDao = new AuditoryDao(jdbcTemplate, daoProperties);
        Auditory auditory = new Auditory(1, "1st floor");
        auditoryDao.save(auditory);

        Auditory result = auditoryDao.findById(1);

        assertEquals(auditory, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        AuditoryDao auditoryDao = new AuditoryDao(jdbcTemplate, daoProperties);
        Auditory auditory = new Auditory(1, "1st floor");
        auditoryDao.save(auditory);
        List<Auditory> itemsFromDb = auditoryDao.findAllRecords();
        assertEquals(1, itemsFromDb.size());

        auditoryDao.deleteById(1);
        itemsFromDb = auditoryDao.findAllRecords();

        assertEquals(0, itemsFromDb.size());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        AuditoryDao auditoryDao = new AuditoryDao(jdbcTemplate, daoProperties);
        Auditory auditory1 = new Auditory(1, "1st floor");
        Auditory auditory2 = new Auditory(2, "Next to WC on the 3rd floor");
        auditoryDao.save(auditory1);
        auditoryDao.save(auditory2);

        List<Auditory> itemsFromDb = auditoryDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(auditory1, itemsFromDb.get(0));
        assertEquals(auditory2, itemsFromDb.get(1));
    }

}