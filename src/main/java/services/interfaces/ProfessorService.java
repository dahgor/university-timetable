package services.interfaces;

import dao.entities.Course;
import dao.entities.Professor;
import services.ServiceException;

import java.util.List;

public interface ProfessorService extends Service<Professor> {

    void changeFirstName(Professor professor, String newName) throws ServiceException;

    void changeLastName(Professor professor, String newName) throws ServiceException;

    void addCourse(Professor professor, Course course) throws ServiceException;

    void deleteCourse(Professor professor, Course course) throws ServiceException;

    List<Course> getCourseList(Professor professor) throws ServiceException;
}
