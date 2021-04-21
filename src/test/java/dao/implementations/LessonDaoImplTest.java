package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Lesson;
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

class LessonDaoImplTest {
    public static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    public static final String PROPERTIES = "./src/test/resources/daoProperties/lessonDao.properties";
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final int INVALID_ID = -1;
    public static final int VALID_ID = 1;
    public static final int TIME_ID = 1;
    public static final Date VALID_DATE = Date.valueOf(LocalDate.now());

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

    void prepareDataForOneLesson() {
        jdbcTemplate.execute(
                "insert into courses(course_name, course_description) VALUES ('math', 'description')");
        jdbcTemplate.execute("insert into professors(first_name, last_name) VALUES ('Alan', 'Smith')");
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-15')");
        jdbcTemplate.execute("insert into auditories(auditory_location) VALUES ('1st floor')");
    }

    void prepareDataForTwoLessons() {
        prepareDataForOneLesson();
        jdbcTemplate.execute(
                "insert into courses(course_name, course_description) VALUES ('biology', 'description')");
        jdbcTemplate.execute("insert into professors(first_name, last_name) VALUES ('Mike', 'Node')");
        jdbcTemplate.execute("insert into groups(group_name) VALUES ('ME-16')");
        jdbcTemplate.execute("insert into auditories(auditory_location) VALUES ('2nd floor')");
    }

    void prepareDataForAssigning() {
        jdbcTemplate.update("insert into time_periods(start_hour, end_hour) VALUES (?, ?)",
                Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));
        jdbcTemplate.update("insert into times(date, time_period_id) VALUES (?, ?)",
                Date.valueOf(LocalDate.now()), 1);
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new LessonDaoImpl(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> lessonDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> lessonDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindScheduledLessonsForTimeMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> lessonDao.findScheduledLessonsForTime(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToAssignLessonToTimeMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> lessonDao.assignLessonToTime(INVALID_ID, INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindLessonsWithCourseMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsWithCourse(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindLessonsWithProfessorMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsWithProfessor(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindLessonsWithGroupMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsWithGroup(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> lessonDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindLessonsWithAuditoryMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsWithAuditory(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidArgsArePassedToFindLessonsForGroupForDayMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception idException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForGroupForDay(INVALID_ID, VALID_DATE));
        Exception nullException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForGroupForDay(VALID_ID, null));
        assertEquals(ID_ERROR, idException.getMessage());
        assertEquals(NULL_ERROR, nullException.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidArgsArePassedToFindLessonsForGroupForMonthMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception idException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForGroupForMonth(INVALID_ID, VALID_DATE));
        Exception nullException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForGroupForMonth(VALID_ID, null));
        assertEquals(ID_ERROR, idException.getMessage());
        assertEquals(NULL_ERROR, nullException.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidArgsArePassedToFindLessonsForProfessorForDayMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception idException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForProfessorForDay(INVALID_ID, VALID_DATE));
        Exception nullException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForProfessorForDay(VALID_ID, null));
        assertEquals(ID_ERROR, idException.getMessage());
        assertEquals(NULL_ERROR, nullException.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidArgsArePassedToFindLessonsForProfessorForMonthMethod() throws DaoException {
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);

        Exception idException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForProfessorForMonth(INVALID_ID, VALID_DATE));
        Exception nullException = assertThrows(DaoException.class,
                () -> lessonDao.findLessonsForProfessorForMonth(VALID_ID, null));
        assertEquals(ID_ERROR, idException.getMessage());
        assertEquals(NULL_ERROR, nullException.getMessage());
    }

    @Test
    void shouldReturnSameCourseFromDbWhenSaved() throws DaoException {
        prepareDataForOneLesson();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson = new Lesson(1, 1, 1, 1, 1);
        lessonDao.save(lesson);
        Lesson result = lessonDao.findById(1);

        assertEquals(lesson, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        prepareDataForOneLesson();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson = new Lesson(1, 1, 1, 1, 1);
        lessonDao.save(lesson);
        List<Lesson> itemsFromDb = lessonDao.findAllRecords();
        assertEquals(1, itemsFromDb.size());

        lessonDao.deleteById(1);
        itemsFromDb = lessonDao.findAllRecords();

        assertEquals(0, itemsFromDb.size());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        prepareDataForTwoLessons();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson1 = new Lesson(1, 1, 1, 1, 1);
        Lesson lesson2 = new Lesson(2, 2, 2, 2, 2);
        lessonDao.save(lesson1);
        lessonDao.save(lesson2);

        List<Lesson> itemsFromDb = lessonDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(lesson1, itemsFromDb.get(0));
        assertEquals(lesson2, itemsFromDb.get(1));
    }

    @Test
    void shouldAssignLessonForTime() throws DaoException {
        prepareDataForOneLesson();
        prepareDataForAssigning();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson = new Lesson(1, 1, 1, 1, 1);
        lessonDao.save(lesson);

        lessonDao.assignLessonToTime(1, TIME_ID);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from schedule");

        assertTrue(result.next());
        assertEquals(1, result.getInt("lesson_id"));
        assertEquals(1, result.getInt("time_id"));
    }

    @Test
    void shouldRetrieveLessonsForGivenTime() throws DaoException {
        prepareDataForTwoLessons();
        prepareDataForAssigning();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson1 = new Lesson(1, 1, 1, 1, 1);
        Lesson lesson2 = new Lesson(2, 2, 2, 2, 2);
        lessonDao.save(lesson1);
        lessonDao.save(lesson2);
        lessonDao.assignLessonToTime(1, TIME_ID);
        lessonDao.assignLessonToTime(2, TIME_ID);

        List<Lesson> result = lessonDao.findScheduledLessonsForTime(TIME_ID);

        assertEquals(2, result.size());
        assertEquals(lesson1, result.get(0));
        assertEquals(lesson2, result.get(1));
    }

    @Test
    void shouldRetrieveLessonsForGivenGroupAndDay() throws DaoException {
        prepareDataForTwoLessons();
        prepareDataForAssigning();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson1 = new Lesson(1, 1, 1, 1, 1);
        Lesson lesson2 = new Lesson(2, 2, 2, 2, 2);
        lessonDao.save(lesson1);
        lessonDao.save(lesson2);
        lessonDao.assignLessonToTime(1, TIME_ID);
        lessonDao.assignLessonToTime(2, TIME_ID);

        List<Lesson> result = lessonDao.findLessonsForGroupForDay(1,
                Date.valueOf(LocalDate.now()));

        assertEquals(1, result.size());
        assertEquals(lesson1, result.get(0));
    }

    @Test
    void shouldRetrieveLessonsForGivenGroupAndMonth() throws DaoException {
        prepareDataForTwoLessons();
        prepareDataForAssigning();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson1 = new Lesson(1, 1, 1, 1, 1);
        Lesson lesson2 = new Lesson(2, 2, 2, 2, 2);
        lessonDao.save(lesson1);
        lessonDao.save(lesson2);
        lessonDao.assignLessonToTime(1, TIME_ID);
        lessonDao.assignLessonToTime(2, TIME_ID);

        List<Lesson> result = lessonDao.findLessonsForGroupForMonth(2,
                Date.valueOf(LocalDate.now()));

        assertEquals(1, result.size());
        assertEquals(lesson2, result.get(0));
    }

    @Test
    void shouldRetrieveLessonsForGivenProfessorAndDay() throws DaoException {
        prepareDataForTwoLessons();
        prepareDataForAssigning();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson1 = new Lesson(1, 1, 1, 1, 1);
        Lesson lesson2 = new Lesson(2, 2, 2, 2, 2);
        lessonDao.save(lesson1);
        lessonDao.save(lesson2);
        lessonDao.assignLessonToTime(1, TIME_ID);
        lessonDao.assignLessonToTime(2, TIME_ID);

        List<Lesson> result = lessonDao.findLessonsForProfessorForDay(1,
                Date.valueOf(LocalDate.now()));

        assertEquals(1, result.size());
        assertEquals(lesson1, result.get(0));
    }

    @Test
    void shouldRetrieveLessonsForGivenProfessorAndMonth() throws DaoException {
        prepareDataForTwoLessons();
        prepareDataForAssigning();
        LessonDaoImpl lessonDao = new LessonDaoImpl(jdbcTemplate, daoProperties);
        Lesson lesson1 = new Lesson(1, 1, 1, 1, 1);
        Lesson lesson2 = new Lesson(2, 2, 2, 2, 2);
        lessonDao.save(lesson1);
        lessonDao.save(lesson2);
        lessonDao.assignLessonToTime(1, TIME_ID);
        lessonDao.assignLessonToTime(2, TIME_ID);

        List<Lesson> result = lessonDao.findLessonsForProfessorForMonth(2,
                Date.valueOf(LocalDate.now()));

        assertEquals(1, result.size());
        assertEquals(lesson2, result.get(0));
    }

}