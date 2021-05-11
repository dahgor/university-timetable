package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Course;
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

class CourseDaoImplTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/courseDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;
    private static final int PROFESSOR_ID = 1;
    private static final int GROUP_ID = 1;

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

    void saveCourse(Course course) {
        jdbcTemplate.update("insert into courses(course_name, course_description) values(?, ?)",
                course.getName(), course.getDescription());
    }

    void createProfessor(String firstName, String lastName) {
        jdbcTemplate.update("insert into professors(first_name, last_name) values (?, ?)",
                firstName, lastName);
    }

    void assignProfessorToCourse(int professorId, int courseId) {
        jdbcTemplate.update("insert into professor_course(professor_id, course_id) values (?, ?)",
                professorId, courseId);
    }

    void createGroup(String groupName) {
        jdbcTemplate.update("insert into groups(group_name) values (?)", groupName);
    }

    void assignGroupToCourse(int groupId, int courseId) {
        jdbcTemplate.update("insert into group_course(group_id, course_id) values (?, ?)",
                groupId, courseId);
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
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToChangeNameMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> courseDao.changeName(INVALID_ID, ""));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToChangeNameMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> courseDao.changeName(1, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToChangeDescriptionMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> courseDao.changeDescription(INVALID_ID, ""));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToChangeDescriptionMethod() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> courseDao.changeDescription(1, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameCourseFromDbWhenSaved() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        courseDao.save(course);

        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from courses");

        assertTrue(result.next());
        assertEquals(course.getId(), result.getInt("course_id"));
        assertEquals(course.getName(), result.getString("course_name"));
        assertEquals(course.getDescription(), result.getString("course_description"));
    }

    @Test
    void shouldReturnCorrectIdWhenSaved() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course1 = new Course(1, "Math", "description");
        Course course2 = new Course(2, "Biology", "description");

        int generatedId1 = courseDao.save(course1);
        int generatedId2 = courseDao.save(course2);

        assertEquals(1, generatedId1);
        assertEquals(2, generatedId2);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        saveCourse(course);

        courseDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from courses");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course1 = new Course(1, "Math", "description");
        Course course2 = new Course(2, "Biology", "description");
        saveCourse(course1);
        saveCourse(course2);

        List<Course> itemsFromDb = courseDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(course1, itemsFromDb.get(0));
        assertEquals(course2, itemsFromDb.get(1));
    }

    @Test
    void shouldReturnCorrectCourseForProfessor() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        saveCourse(course);
        createProfessor("Alan", "Smith");
        assignProfessorToCourse(PROFESSOR_ID, course.getId());

        List<Course> result = courseDao.findByProfessor(1);

        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
    }

    @Test
    void shouldReturnCorrectCourseForGroup() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        saveCourse(course);
        createGroup("ME-15");
        assignGroupToCourse(GROUP_ID, course.getId());

        List<Course> result = courseDao.findByGroup(1);

        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
    }

    @Test
    void shouldChangeNameWhenValidArgsArePassed() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        saveCourse(course);
        String newName = "Physics";

        courseDao.changeName(1, newName);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from courses");

        assertTrue(result.next());
        assertEquals(newName, result.getString("course_name"));
    }

    @Test
    void shouldChangeDescriptionWhenValidArgsArePassed() throws DaoException {
        CourseDaoImpl courseDao = new CourseDaoImpl(jdbcTemplate, daoProperties);
        Course course = new Course(1, "Math", "description");
        saveCourse(course);
        String newDescription = "new description";

        courseDao.changeDescription(1, newDescription);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from courses");

        assertTrue(result.next());
        assertEquals(newDescription, result.getString("course_description"));
    }

}