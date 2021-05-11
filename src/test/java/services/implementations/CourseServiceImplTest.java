package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.interfaces.CourseDao;
import dao.interfaces.GroupDao;
import dao.interfaces.ProfessorDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.CourseService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;

    @Mock
    private CourseDao courseDao;
    @Mock
    private ProfessorDao professorDao;
    @Mock
    private GroupDao groupDao;

    private CourseService courseService;

    @BeforeEach
    void prepareCorrectCourseServiceClass() throws ServiceException {
        courseService = new CourseServiceImpl(courseDao, professorDao, groupDao);
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new CourseServiceImpl(null, null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> courseService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> courseService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidIdIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> courseService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeNameMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> courseService.changeName(null, null));

        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeDescriptionMethod() {
        Exception exception1 = assertThrows(ServiceException.class,
                () -> courseService.changeDescription(null, ""));
        Exception exception2 = assertThrows(ServiceException.class,
                () -> courseService.changeDescription(new Course(), null));

        assertEquals(NULL_ERROR, exception1.getMessage());
        assertEquals(NULL_ERROR, exception2.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIsPassedToGetProfessorListMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> courseService.getProfessorList(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIsPassedToGetGroupListMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> courseService.getGroupList(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldSaveItemWhenValidArgsArePassed() throws ServiceException, DaoException {
        Course course = new Course("Math", "Description");
        when(courseDao.save(course)).thenReturn(1);

        Course result = courseService.save(course);

        verify(courseDao).save(course);
        assertEquals(course.getName(), result.getName());
        assertEquals(course.getDescription(), result.getDescription());
    }

    @Test
    void shouldDeleteItemWhenValidArgIsPassed() throws ServiceException, DaoException {
        Course course = new Course(1, "Math", "Description");

        courseService.delete(course);

        verify(courseDao).deleteById(course.getId());
    }

    @Test
    void shouldReturnItemById() throws ServiceException, DaoException {
        Course course = new Course(1, "Math", "Description");
        when(courseDao.findById(1)).thenReturn(course);

        Course result = courseService.findById(1);

        verify(courseDao).findById(1);
        assertEquals(course, result);
    }

    @Test
    void shouldReturnAllItems() throws ServiceException, DaoException {
        List<Course> courses = new LinkedList<>();
        courses.add(new Course(1, "Math", "Description"));
        courses.add(new Course(2, "Physics", "Description"));
        when(courseDao.findAllRecords()).thenReturn(courses);

        List<Course> result = courseService.getAllItems();

        verify(courseDao).findAllRecords();
        assertEquals(courses, result);
    }

    @Test
    void shouldChangeNameWhenValidArgsArePassed() throws ServiceException, DaoException {
        Course course = new Course(1, "Math", "Description");
        String newName = "Physics";

        courseService.changeName(course, newName);

        verify(courseDao).changeName(course.getId(), newName);
    }

    @Test
    void shouldChangeDescriptionWhenValidArgsArePassed() throws ServiceException, DaoException {
        Course course = new Course(1, "Math", "Description");
        String newDescription = "New description";

        courseService.changeDescription(course, newDescription);

        verify(courseDao).changeDescription(course.getId(), newDescription);
    }

    @Test
    void shouldReturnProfessorsWhenValidCourseIsPassed() throws ServiceException, DaoException {
        Course course = new Course(1, "Math", "Description");

        courseService.getProfessorList(course);

        verify(professorDao).findByCourse(course.getId());
    }

    @Test
    void shouldReturnGroupsWhenValidCourseIsPassed() throws ServiceException, DaoException {
        Course course = new Course(1, "Math", "Description");

        courseService.getGroupList(course);

        verify(groupDao).findByCourse(course.getId());
    }

}