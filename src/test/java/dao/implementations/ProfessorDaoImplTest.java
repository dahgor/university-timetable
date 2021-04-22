package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Professor;
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

class ProfessorDaoImplTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/professorDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;
    private static final int COURSE_ID = 1;

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

    void saveProfessor(Professor professor) {
        jdbcTemplate.update("insert into professors(first_name, last_name) values (?, ?)",
                professor.getFirstName(), professor.getLastName());
    }

    void assignProfessorToCourse(int professorId, int courseId) {
        jdbcTemplate.update("insert into professor_course(professor_id, course_id) values(?, ?)",
                professorId, courseId);
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
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from professors");

        assertTrue(result.next());
        assertEquals(1, result.getInt("professor_id"));
        assertEquals(professor.getFirstName(), result.getString("first_name"));
        assertEquals(professor.getLastName(), result.getString("last_name"));
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor = new Professor(1, "Alan", "Smith");
        saveProfessor(professor);

        professorDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from professors");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor1 = new Professor(1, "Alan", "Smith");
        Professor professor2 = new Professor(2, "John", "Walker");
        saveProfessor(professor1);
        saveProfessor(professor2);

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
        saveProfessor(professor);

        professorDao.assignProfessorToCourse(1, COURSE_ID);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from professor_course");

        assertTrue(result.next());
        assertEquals(professor.getId(), result.getInt("professor_id"));
        assertEquals(COURSE_ID, result.getInt("course_id"));
    }

    @Test
    void shouldSearchProfessorsForGivenCourse() throws DaoException {
        prepareDataForAssigning();
        ProfessorDaoImpl professorDao = new ProfessorDaoImpl(jdbcTemplate, daoProperties);
        Professor professor1 = new Professor(1, "Alan", "Smith");
        Professor professor2 = new Professor(2, "John", "Walker");
        saveProfessor(professor1);
        saveProfessor(professor2);
        assignProfessorToCourse(professor1.getId(), COURSE_ID);
        assignProfessorToCourse(professor2.getId(), COURSE_ID);

        List<Professor> result = professorDao.findProfessorsForCourse(COURSE_ID);

        assertEquals(2, result.size());
        assertEquals(professor1, result.get(0));
        assertEquals(professor2, result.get(1));
    }

}