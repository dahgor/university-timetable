package services.interfaces;

import dao.entities.Course;
import dao.entities.Group;
import dao.entities.Student;
import services.ServiceException;

import java.util.List;

public interface GroupService extends Service<Group> {

    void changeName(Group group, String newName) throws ServiceException;

    void addCourse(Group group, Course course) throws ServiceException;

    void deleteCourse(Group group, Course course) throws ServiceException;

    List<Course> getCourseList(Group group) throws ServiceException;

    List<Student> getStudentList(Group group) throws ServiceException;
}
