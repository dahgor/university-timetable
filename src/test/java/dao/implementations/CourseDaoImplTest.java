package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Auditory;
import dao.entities.Course;
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

class CourseDaoImplTest {
    public static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    public static final String PROPERTIES = "./src/test/resources/daoProperties/courseDao.properties";
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
        FileInputStream file = new FileInputStream(PROPERTIES);
        daoProperties = new DaoProperties(file);
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new CourseDaoImpl(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> courseDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> courseDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> courseDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameCourseFromDbWhenSaved() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        courseDao.save(course);
        Course result = courseDao.findById(1);

        assertEquals(course, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        courseDao.save(course);
        List<Course> itemsFromDb = courseDao.findAllRecords();
        assertEquals(1, itemsFromDb.size());

        courseDao.deleteById(1);
        itemsFromDb = courseDao.findAllRecords();

        assertEquals(0, itemsFromDb.size());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course1 = new Course(1, "Math", "description");
        Course course2 = new Course(2, "Biology", "description");
        courseDao.save(course1);
        courseDao.save(course2);

        List<Course> itemsFromDb = courseDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(course1, itemsFromDb.get(0));
        assertEquals(course2, itemsFromDb.get(1));
    }

    @Test
    void shouldReturnCorrectCourseForProfessor() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        courseDao.save(course);
        jdbcTemplate.update("insert into professors(first_name, last_name) values ('Alan', 'Smith')");
        jdbcTemplate.update("insert into professor_course(professor_id, course_id) values (1, 1)");

        List<Course> result = courseDao.findCoursesForProfessor(1);

        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
    }

    @Test
    void shouldReturnCorrectCourseForGroup() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        courseDao.save(course);
        jdbcTemplate.update("insert into groups(group_name) values ('ME-15')");
        jdbcTemplate.update("insert into group_course(group_id, course_id) values (1, 1)");

        List<Course> result = courseDao.findCoursesForGroup(1);

        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
    }

}