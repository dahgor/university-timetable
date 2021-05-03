package services.implementations;

import dao.DaoException;
import dao.entities.Group;
import dao.entities.Student;
import dao.interfaces.GroupDao;
import dao.interfaces.StudentDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.StudentService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;
    private static final int GROUP_ID = 1;

    @Mock
    private StudentDao studentDao;
    @Mock
    private GroupDao groupDao;

    @Test
    void shouldThrowExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new StudentServiceImpl(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeFirstNameMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.changeFirstName(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeLastNameMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.changeLastName(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeGroupMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.changeGroup(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetGroupMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.getGroup(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToSaveMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws ServiceException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> studentService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldSaveItemWhenValidArgsArePassed() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student("John", "Snow", GROUP_ID);
        when(studentDao.save(student)).thenReturn(1);
        InOrder callOrder = Mockito.inOrder(studentDao);

        Student result = studentService.save(student);

        verify(studentDao).save(student);
        assertEquals(student.getFirstName(), result.getFirstName());
        assertEquals(student.getLastName(), result.getLastName());
        assertEquals(student.getGroupId(), result.getGroupId());
        assertEquals(1, result.getId());
    }

    @Test
    void shouldDeleteItemWhenValidArgIsPassed() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student(1, GROUP_ID, "John", "Snow");
        InOrder callOrder = Mockito.inOrder(studentDao);

        studentService.delete(student);

        verify(studentDao).deleteById(student.getId());
    }

    @Test
    void shouldReturnItemById() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student(1, GROUP_ID, "John", "Snow");
        InOrder callOrder = Mockito.inOrder(studentDao);
        when(studentDao.findById(1)).thenReturn(student);

        Student result = studentService.findById(1);

        verify(studentDao).findById(1);
        assertEquals(student, result);
    }

    @Test
    void shouldReturnAllItems() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        InOrder callOrder = Mockito.inOrder(studentDao);
        List<Student> students = new LinkedList<>();
        students.add(new Student(1, GROUP_ID, "John", "Snow"));
        students.add(new Student(2, GROUP_ID, "Mike", "Sun"));
        when(studentDao.findAllRecords()).thenReturn(students);

        List<Student> result = studentService.getAllItems();

        verify(studentDao).findAllRecords();
        assertEquals(students, result);
    }

    @Test
    void shouldChangeFirstNameWhenValidArgIsPassed() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student(1, GROUP_ID, "John", "Snow");
        String newName = "Mike";
        InOrder callOrder = Mockito.inOrder(studentDao);

        studentService.changeFirstName(student, newName);

        verify(studentDao).changeFirstName(student.getId(), newName);
    }

    @Test
    void shouldChangeLastNameWhenValidArgIsPassed() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student(1, GROUP_ID, "John", "Snow");
        String newName = "Smith";
        InOrder callOrder = Mockito.inOrder(studentDao);

        studentService.changeLastName(student, newName);

        verify(studentDao).changeLastName(student.getId(), newName);
    }

    @Test
    void shouldChangeGroupWhenValidArgIsPassed() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student(1, GROUP_ID, "John", "Snow");
        Group newGroup = new Group(2, "ME-15");
        InOrder callOrder = Mockito.inOrder(studentDao);

        studentService.changeGroup(student, newGroup);

        verify(studentDao).changeGroup(student.getId(), newGroup.getId());
    }

    @Test
    void shouldReturnGroupWhenValidArgIsPassed() throws ServiceException, DaoException {
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        Student student = new Student(1, GROUP_ID, "John", "Snow");
        Group group = new Group(GROUP_ID, "ME-15");
        InOrder callOrder = Mockito.inOrder(studentDao);
        when(groupDao.findById(student.getGroupId())).thenReturn(group);

        Group result = studentService.getGroup(student);

        verify(groupDao).findById(student.getGroupId());
        assertEquals(group, result);
    }
}