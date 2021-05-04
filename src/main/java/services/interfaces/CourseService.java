package services.interfaces;

import dao.entities.Course;
import dao.entities.Group;
import dao.entities.Professor;
import services.ServiceException;

import java.util.List;

public interface CourseService extends Service<Course> {

    void changeName(Course course, String newName) throws ServiceException;

    void changeDescription(Course course, String newDescription) throws ServiceException;

    List<Professor> getProfessorList(Course course) throws ServiceException;

    List<Group> getGroupList(Course course) throws ServiceException;
}
