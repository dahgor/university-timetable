package services.interfaces;

import dao.entities.Group;
import dao.entities.Student;
import services.ServiceException;

public interface StudentService extends Service<Student> {

    void changeFirstName(Student student, String newName) throws ServiceException;

    void changeLastName(Student student, String newName) throws ServiceException;

    void changeGroup(Student student, Group newGroup) throws ServiceException;

    Group getGroup(Student student) throws ServiceException;
}
