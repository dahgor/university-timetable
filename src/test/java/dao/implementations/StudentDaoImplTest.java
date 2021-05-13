package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Student;
import dao.interfaces.StudentDao;
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
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/studentDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id is passed";
    private static final int INVALID_ID = -1;
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

    private void prepareGroup() {
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-15')");
    }

    private void prepareTwoGroups() {
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-15')");
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-16')");
    }

    private void saveStudent(Student student) {
        jdbcTemplate.update("insert into students(group_id, first_name, last_name) values (?, ?, ?)",
                student.getGroupId(), student.getFirstName(), student.getLastName());
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
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToChangeGroupMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> studentDao.changeGroup(INVALID_ID, INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToChangeFirstNameMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> studentDao.changeFirstName(INVALID_ID, ""));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToChangeFirstNameMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> studentDao.changeFirstName(1, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToChangeLastNameMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> studentDao.changeLastName(INVALID_ID, ""));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToChangeLastNameMethod() throws DaoException {
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> studentDao.changeLastName(1, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameStudentFromDbWhenSaved() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, GROUP_ID, "Mike", "Chelsey");

        studentDao.save(student);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from students");

        assertTrue(result.next());
        assertEquals(student.getId(), result.getInt("student_id"));
        assertEquals(student.getFirstName(), result.getString("first_name"));
        assertEquals(student.getLastName(), result.getString("last_name"));
        assertEquals(student.getGroupId(), result.getInt("group_id"));
    }

    @Test
    void shouldReturnCorrectIdWhenSaved() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student1 = new Student(1, GROUP_ID, "Mike", "Chelsey");
        Student student2 = new Student(2, GROUP_ID, "Chen", "McFoos");

        int generatedId1 = studentDao.save(student1);
        int generatedId2 = studentDao.save(student2);

        assertEquals(1, generatedId1);
        assertEquals(2, generatedId2);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        prepareGroup();
        StudentDaoImpl studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, GROUP_ID, "Mike", "Chelsey");
        saveStudent(student);

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
        saveStudent(student1);
        saveStudent(student2);

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
        saveStudent(student1);
        saveStudent(student2);

        List<Student> itemsFromDb = studentDao.findByGroup(GROUP_ID);

        assertEquals(2, itemsFromDb.size());
        assertEquals(student1, itemsFromDb.get(0));
        assertEquals(student2, itemsFromDb.get(1));
    }

    @Test
    void shouldChangeGroupWhenValidIdsArePassed() throws DaoException {
        prepareTwoGroups();
        StudentDao studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, 1, "Mike", "Chelsey");
        saveStudent(student);

        studentDao.changeGroup(1, 2);
        SqlRowSet result = jdbcTemplate.queryForRowSet(
                "select * from students where student_id = 1");

        assertTrue(result.next());
        assertEquals(2, result.getInt("group_id"));
    }

    @Test
    void shouldChangeFirstNameWhenValidArgsArePassed() throws DaoException {
        prepareGroup();
        StudentDao studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, 1, "Mike", "Chelsey");
        saveStudent(student);
        String newName = "John";

        studentDao.changeFirstName(1, newName);
        SqlRowSet result = jdbcTemplate.queryForRowSet(
                "select * from students where student_id = 1");

        assertTrue(result.next());
        assertEquals(newName, result.getString("first_name"));
    }

    @Test
    void shouldChangeLastNameWhenValidArgsArePassed() throws DaoException {
        prepareGroup();
        StudentDao studentDao = new StudentDaoImpl(jdbcTemplate, daoProperties);
        Student student = new Student(1, 1, "Mike", "Chelsey");
        saveStudent(student);
        String newName = "Lebovski";

        studentDao.changeLastName(1, newName);
        SqlRowSet result = jdbcTemplate.queryForRowSet(
                "select * from students where student_id = 1");

        assertTrue(result.next());
        assertEquals(newName, result.getString("last_name"));
    }

}