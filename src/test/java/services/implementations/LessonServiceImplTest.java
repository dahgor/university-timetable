package services.implementations;

import dao.DaoException;
import dao.entities.*;
import dao.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.LessonService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;
    private static final int COURSE = 1;
    private static final int GROUP = 1;
    private static final int PROFESSOR = 1;
    private static final int AUDITORY = 1;
    private static final Date DATE = Date.valueOf(LocalDate.now());
    private static final int TIME_PERIOD = 1;

    @Mock
    private LessonDao lessonDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private ProfessorDao professorDao;
    @Mock
    private AuditoryDao auditoryDao;

    private LessonService lessonService;
    private List<Lesson> lessonList;

    @BeforeEach
    void prepareLessonService() throws ServiceException {
        lessonService = new LessonServiceImpl(lessonDao, groupDao, courseDao, professorDao,
                auditoryDao);
    }

    @BeforeEach
    void prepareLessonList() {
        lessonList = new LinkedList<>();
        lessonList.add(new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY));
        lessonList.add(new Lesson(2, COURSE, PROFESSOR, GROUP, AUDITORY));
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new LessonServiceImpl(null, null, null, null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> lessonService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> lessonService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidIdIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> lessonService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeAuditoryMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.changeAuditory(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeGroupMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.changeGroup(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeProfessorMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.changeProfessor(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeCourseMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.changeCourse(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeTimeMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.changeTime(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToAssignTimeMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.assignTime(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetAuditoryMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getAuditory(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetGroupMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getGroup(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetProfessorMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getProfessor(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetCourseMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getCourse(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetAllForTimeMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getAllForTime(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetAllForGroupAndDateMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getAllForGroupAndDate(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetAllForGroupAndMonthMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getAllForGroupAndDate(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetAllForProfessorAndDateMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getAllForProfessorAndDate(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetAllForProfessorAndMonthMethod() {
        Exception exception = assertThrows(ServiceException.class, () ->
                lessonService.getAllForProfessorAndMonth(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldSaveItemWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(COURSE, PROFESSOR, GROUP, AUDITORY);
        when(lessonDao.save(lesson)).thenReturn(1);

        Lesson result = lessonService.save(lesson);

        verify(lessonDao).save(lesson);
        assertEquals(lesson.getCourseId(), result.getCourseId());
        assertEquals(lesson.getProfessorId(), result.getProfessorId());
        assertEquals(lesson.getGroupId(), result.getGroupId());
        assertEquals(lesson.getAuditoryId(), result.getAuditoryId());
    }

    @Test
    void shouldDeleteItemWhenValidArgIsPassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);

        lessonService.delete(lesson);

        verify(lessonDao).deleteById(lesson.getId());
    }

    @Test
    void shouldReturnItemById() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        when(lessonDao.findById(1)).thenReturn(lesson);

        Lesson result = lessonService.findById(1);

        verify(lessonDao).findById(1);
        assertEquals(lesson, result);
    }

    @Test
    void shouldReturnAllItems() throws ServiceException, DaoException {
        when(lessonDao.findAllRecords()).thenReturn(lessonList);

        List<Lesson> result = lessonService.getAllItems();

        verify(lessonDao).findAllRecords();
        assertEquals(lessonList, result);
    }

    @Test
    void shouldChangeAuditoryWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Auditory newAuditory = new Auditory(1, "1st floor");

        lessonService.changeAuditory(lesson, newAuditory);

        verify(lessonDao).changeAuditory(lesson.getId(), newAuditory.getId());
    }

    @Test
    void shouldChangeCourseWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Course newCourse = new Course(1, "Math", "Description");

        lessonService.changeCourse(lesson, newCourse);

        verify(lessonDao).changeCourse(lesson.getId(), newCourse.getId());
    }

    @Test
    void shouldChangeGroupWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Group newGroup = new Group(1, "ME-15");

        lessonService.changeGroup(lesson, newGroup);

        verify(lessonDao).changeGroup(lesson.getId(), newGroup.getId());
    }

    @Test
    void shouldChangeProfessorWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Professor newProfessor = new Professor(1, "John", "Snow");

        lessonService.changeProfessor(lesson, newProfessor);

        verify(lessonDao).changeProfessor(lesson.getId(), newProfessor.getId());
    }

    @Test
    void shouldChangeTimeWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Time newTime = new Time(1, DATE, TIME_PERIOD);

        lessonService.changeTime(lesson, newTime);

        verify(lessonDao).changeTime(lesson.getId(), newTime.getId());
    }

    @Test
    void shouldAssignTimeWhenValidArgsArePassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Time time = new Time(1, DATE, TIME_PERIOD);

        lessonService.assignTime(lesson, time);

        verify(lessonDao).assignLessonToTime(lesson.getId(), time.getId());
    }

    @Test
    void shouldReturnAuditoryWhenValidArgIsPassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Auditory auditory = new Auditory(1, "1st floor");
        when(auditoryDao.findById(lesson.getAuditoryId())).thenReturn(auditory);

        Auditory result = lessonService.getAuditory(lesson);

        verify(auditoryDao).findById(lesson.getAuditoryId());
        assertEquals(auditory, result);
    }

    @Test
    void shouldReturnGroupWhenValidArgIsPassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Group group = new Group(1, "ME-15");
        when(groupDao.findById(lesson.getGroupId())).thenReturn(group);

        Group result = lessonService.getGroup(lesson);

        verify(groupDao).findById(lesson.getGroupId());
        assertEquals(group, result);
    }

    @Test
    void shouldReturnCourseWhenValidArgIsPassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Course course = new Course(1, "Math", "Description");
        when(courseDao.findById(lesson.getCourseId())).thenReturn(course);

        Course result = lessonService.getCourse(lesson);

        verify(courseDao).findById(lesson.getCourseId());
        assertEquals(course, result);
    }

    @Test
    void shouldReturnProfessorWhenValidArgIsPassed() throws ServiceException, DaoException {
        Lesson lesson = new Lesson(1, COURSE, PROFESSOR, GROUP, AUDITORY);
        Professor professor = new Professor(1, "John", "Snow");
        when(professorDao.findById(lesson.getProfessorId())).thenReturn(professor);

        Professor result = lessonService.getProfessor(lesson);

        verify(professorDao).findById(lesson.getProfessorId());
        assertEquals(professor, result);
    }

    @Test
    void shouldReturnLessonsWhenValidArgIsPassedToGetAllForTimeMethod() throws ServiceException, DaoException {
        Time time = new Time(1, DATE, TIME_PERIOD);
        when(lessonDao.findByTime(time.getId())).thenReturn(lessonList);

        List<Lesson> result = lessonService.getAllForTime(time);

        verify(lessonDao).findByTime(time.getId());
        assertEquals(lessonList, result);
    }

    @Test
    void shouldReturnLessonsWhenValidArgIsPassedToGetAllForGroupAndDateMethod() throws ServiceException, DaoException {
        Group group = new Group(1, "ME-15");
        when(lessonDao.findByGroupAndDate(group.getId(), DATE)).thenReturn(lessonList);

        List<Lesson> result = lessonService.getAllForGroupAndDate(group, DATE);

        verify(lessonDao).findByGroupAndDate(group.getId(), DATE);
        assertEquals(lessonList, result);
    }

    @Test
    void shouldReturnLessonsWhenValidArgIsPassedToGetAllForGroupAndMonthMethod() throws ServiceException, DaoException {
        Group group = new Group(1, "ME-15");
        when(lessonDao.findByGroupAndMonth(group.getId(), DATE)).thenReturn(lessonList);

        List<Lesson> result = lessonService.getAllForGroupAndMonth(group, DATE);

        verify(lessonDao).findByGroupAndMonth(group.getId(), DATE);
        assertEquals(lessonList, result);
    }

    @Test
    void shouldReturnLessonsWhenValidArgIsPassedToGetAllForProfessorAndDateMethod() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        when(lessonDao.findByProfessorAndDate(professor.getId(), DATE)).thenReturn(lessonList);

        List<Lesson> result = lessonService.getAllForProfessorAndDate(professor, DATE);

        verify(lessonDao).findByProfessorAndDate(professor.getId(), DATE);
        assertEquals(lessonList, result);
    }

    @Test
    void shouldReturnLessonsWhenValidArgIsPassedToGetAllForProfessorAndMonthMethod() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        when(lessonDao.findByProfessorAndMonth(professor.getId(), DATE)).thenReturn(lessonList);

        List<Lesson> result = lessonService.getAllForProfessorAndMonth(professor, DATE);

        verify(lessonDao).findByProfessorAndMonth(professor.getId(), DATE);
        assertEquals(lessonList, result);
    }

}