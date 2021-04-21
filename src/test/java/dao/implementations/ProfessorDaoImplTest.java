package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Professor;
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

class ProfessorDaoImplTest {
    public static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    public static final String PROPERTIES = "./src/test/resources/daoProperties/professorDao.properties";
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final int INVALID_ID = -1;
    public static final int COURSE_ID = 1;

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

    void prepareDataForAssigning() {
        jdbcTemplate.execute("insert into courses(course_name, course_description) " +
                "VALUES ('Math', 'description')");
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class,
                () -> new ProfessorDaoImpl(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> professorDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> professorDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> professorDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameAuditoryFromDbWhenSaved() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor = new Professor(1, "Alan", "Smith");
        professorDao.save(professor);

        Professor result = professorDao.findById(1);

        assertEquals(professor, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor = new Professor(1, "Alan", "Smith");
        professorDao.save(professor);
        List<Professor> itemsFromDb = professorDao.findAllRecords();
        assertEquals(1, itemsFromDb.size());

        professorDao.deleteById(1);
        itemsFromDb = professorDao.findAllRecords();

        assertEquals(0, itemsFromDb.size());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor1 = new Professor(1, "Alan", "Smith");
        Professor professor2 = new Professor(2, "John", "Walker");
        professorDao.save(professor1);
        professorDao.save(professor2);

        List<Professor> itemsFromDb = professorDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(professor1, itemsFromDb.get(0));
        assertEquals(professor2, itemsFromDb.get(1));
    }

    @Test
    void shouldAssignProfessorToCourse() throws DaoException {
        prepareDataForAssigning();
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor = new Professor(1, "Alan", "Smith");
        professorDao.save(professor);

        assertDoesNotThrow(() -> professorDao.assignProfessorToCourse(1, COURSE_ID));
    }

    @Test
    void shouldSearchProfessorsForGivenCourse() throws DaoException {
        prepareDataForAssigning();
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor1 = new Professor(1, "Alan", "Smith");
        Professor professor2 = new Professor(2, "John", "Walker");
        professorDao.save(professor1);
        professorDao.save(professor2);
        professorDao.assignProfessorToCourse(1, COURSE_ID);
        professorDao.assignProfessorToCourse(2, COURSE_ID);

        List<Professor> result = professorDao.findProfessorsForCourse(COURSE_ID);

        assertEquals(2, result.size());
        assertEquals(professor1, result.get(0));
        assertEquals(professor2, result.get(1));
    }

}