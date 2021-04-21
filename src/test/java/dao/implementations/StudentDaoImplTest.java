package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Student;
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

class StudentDaoImplTest {
    public static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    public static final String PROPERTIES = "./src/test/resources/daoProperties/studentDao.properties";
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id is passed";
    public static final int INVALID_ID = -1;
    public static final int GROUP_ID = 1;

    private JdbcTemplate jdbcTemplate;
    private DaoProperties daoProperties;

    @BeforeEach
    void prepareJdbcAndProperties() throws FileNotFoundException {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        FileInputStream file = new FileInputStream(PROPERTIES);
        daoProperties = new DaoProperties(file);
    }

    void prepareGroup() {
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-15')");
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new StudentDaoImpl(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> studentDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> studentDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> studentDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameStudentFromDbWhenSaved() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, GROUP_ID, "Mike", "Chelsey");
        studentDao.save(student);

        Student result = studentDao.findById(1);

        assertEquals(student, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, GROUP_ID, "Mike", "Chelsey");
        studentDao.save(student);

        studentDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from students");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student1 = new Student(1, GROUP_ID, "Mike", "Chelsey");
        Student student2 = new Student(2, GROUP_ID, "Chen", "McFoos");
        studentDao.save(student1);
        studentDao.save(student2);

        List<Student> itemsFromDb = studentDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(student1, itemsFromDb.get(0));
        assertEquals(student2, itemsFromDb.get(1));
    }

    @Test
    void shouldReturnStudentsForGivenGroup() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student1 = new Student(1, GROUP_ID, "Mike", "Chelsey");
        Student student2 = new Student(2, GROUP_ID, "Chen", "McFoos");
        studentDao.save(student1);
        studentDao.save(student2);

        List<Student> itemsFromDb = studentDao.findStudentsInGroup(GROUP_ID);

        assertEquals(2, itemsFromDb.size());
        assertEquals(student1, itemsFromDb.get(0));
        assertEquals(student2, itemsFromDb.get(1));
    }

}