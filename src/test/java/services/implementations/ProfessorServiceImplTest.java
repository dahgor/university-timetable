package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Professor;
import dao.interfaces.CourseDao;
import dao.interfaces.ProfessorDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.ProfessorService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;

    @Mock
    private ProfessorDao professorDao;
    @Mock
    private CourseDao courseDao;

    private ProfessorService professorService;

    @BeforeEach
    void prepareCorrectProfessorService() throws ServiceException {
        professorService = new ProfessorServiceImpl(professorDao, courseDao);
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new ProfessorServiceImpl(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToSaveMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidIdIsPassedToFindByIdMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeFirstNameMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.changeFirstName(null, null));

        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeLastNameMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.changeLastName(null, null));

        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToAddCourseMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.addCourse(null, null));

        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteCourseMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.deleteCourse(null, null));

        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetCourseListMethod() {
        Exception exception = assertThrows(ServiceException.class,
                () -> professorService.getCourseList(null));

        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldSaveItemWhenValidArgsArePassed() throws ServiceException, DaoException {
        Professor professor = new Professor("John", "Snow");
        when(professorDao.save(professor)).thenReturn(1);

        Professor result = professorService.save(professor);

        verify(professorDao).save(professor);
        assertEquals(professor.getFirstName(), result.getFirstName());
        assertEquals(professor.getLastName(), result.getLastName());
    }

    @Test
    void shouldDeleteItemWhenValidArgIsPassed() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");

        professorService.delete(professor);

        verify(professorDao).deleteById(professor.getId());
    }

    @Test
    void shouldReturnItemById() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        when(professorDao.findById(1)).thenReturn(professor);

        Professor result = professorService.findById(1);

        verify(professorDao).findById(1);
        assertEquals(professor, result);
    }

    @Test
    void shouldReturnAllItems() throws ServiceException, DaoException {
        List<Professor> professors = new LinkedList<>();
        professors.add(new Professor(1, "John", "Snow"));
        professors.add(new Professor(2, "Mike", "Sun"));
        when(professorDao.findAllRecords()).thenReturn(professors);

        List<Professor> result = professorService.getAllItems();

        verify(professorDao).findAllRecords();
        assertEquals(professors, result);
    }

    @Test
    void shouldChangeNameWhenValidArgsArePassed() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        String newName = "Mike";

        professorService.changeFirstName(professor, newName);

        verify(professorDao).changeFirstName(professor.getId(), newName);
    }

    @Test
    void shouldChangeDescriptionWhenValidArgsArePassed() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        String newName = "Sun";

        professorService.changeLastName(professor, newName);

        verify(professorDao).changeLastName(professor.getId(), newName);
    }

    @Test
    void shouldReturnProfessorsWhenValidProfessorIsPassed() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");

        professorService.getCourseList(professor);

        verify(courseDao).findByProfessor(professor.getId());
    }

    @Test
    void shouldAddCourseWhenValidArgsArePassed() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        Course course = new Course(1, "Math", "Description");

        professorService.addCourse(professor, course);

        verify(professorDao).assignProfessorToCourse(professor.getId(), course.getId());
    }

    @Test
    void shouldDeleteCourseWhenValidArgsArePassed() throws ServiceException, DaoException {
        Professor professor = new Professor(1, "John", "Snow");
        Course course = new Course(1, "Math", "Description");

        professorService.deleteCourse(professor, course);

        verify(professorDao).deleteProfessorFromCourse(professor.getId(), course.getId());
    }

}