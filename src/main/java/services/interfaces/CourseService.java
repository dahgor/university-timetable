package services.interfaces;

import dao.entities.Course;
import dao.entities.Group;
import dao.entities.Professor;
import services.ServiceException;

import java.rmi.ServerException;
import java.util.List;

public interface CourseService extends Service<Course> {

    void changeName(Course course, String newName) throws ServerException;

    void changeDescription(Course course, String newDescription) throws ServiceException;

    List<Professor> getProfessorList(Course course) throws ServiceException;

    List<Group> getGroupList(Group group) throws ServiceException;
}
