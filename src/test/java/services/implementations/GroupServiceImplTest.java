package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Group;
import dao.interfaces.CourseDao;
import dao.interfaces.GroupDao;
import dao.interfaces.StudentDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ServiceException;
import services.interfaces.GroupService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;
    private static final int GROUP_ID = 1;

    @Mock
    private StudentDao studentDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private CourseDao courseDao;

    @Test
    void shouldThrowExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(ServiceException.class,
                () -> new GroupServiceImpl(null, null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToChangeNameMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.changeName(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToAddCourseMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.addCourse(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteCourseMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.deleteCourse(null, null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetCourseListMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.getCourseList(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToGetStudentListMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.getStudentList(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToSaveMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNullIsPassedToDeleteMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.delete(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws ServiceException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldSaveItemWhenValidArgsArePassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group("ME-15");
        when(groupDao.save(group)).thenReturn(1);

        Group result = groupService.save(group);

        verify(groupDao).save(group);
        assertEquals(group.getName(), result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void shouldDeleteItemWhenValidArgIsPassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");

        groupService.delete(group);

        verify(groupDao).deleteById(group.getId());
    }

    @Test
    void shouldReturnItemById() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");
        when(groupDao.findById(1)).thenReturn(group);

        Group result = groupService.findById(1);

        verify(groupDao).findById(1);
        assertEquals(group, result);
    }

    @Test
    void shouldReturnAllItems() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        List<Group> groups = new LinkedList<>();
        groups.add(new Group(1, "ME-15"));
        groups.add(new Group(2, "ME-16"));
        when(groupDao.findAllRecords()).thenReturn(groups);

        List<Group> result = groupService.getAllItems();

        verify(groupDao).findAllRecords();
        assertEquals(groups, result);
    }

    @Test
    void shouldChangeNameWhenValidArgsArePassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");
        String newName = "ME-17";

        groupService.changeName(group, newName);

        verify(groupDao).changeName(group.getId(), newName);
    }

    @Test
    void shouldAddCourseWhenValidArgsArePassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");
        Course course = new Course(1, "Math", "description");

        groupService.addCourse(group, course);

        verify(groupDao).assignGroupToCourse(group.getId(), course.getId());
    }

    @Test
    void shouldDeleteCourseWhenValidArgsArePassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");
        Course course = new Course(1, "Math", "description");

        groupService.deleteCourse(group, course);

        verify(groupDao).deleteGroupFromCourse(group.getId(), course.getId());
    }

    @Test
    void shouldReturnCoursesWhenValidArgsArePassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");

        groupService.getCourseList(group);

        verify(courseDao).findByGroup(group.getId());
    }

    @Test
    void shouldReturnStudentsWhenValidArgsArePassed() throws ServiceException, DaoException {
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao, courseDao);
        Group group = new Group(1, "ME-15");

        groupService.getStudentList(group);

        verify(studentDao).findByGroup(group.getId());
    }
}