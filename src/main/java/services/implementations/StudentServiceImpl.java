package services.implementations;

import dao.DaoException;
import dao.entities.Group;
import dao.entities.Student;
import dao.interfaces.GroupDao;
import dao.interfaces.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.StudentService;

import java.util.List;

@Component("studentService")
public class StudentServiceImpl implements StudentService {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final String DAO_ERROR = "Dao error occurred";

    private StudentDao studentDao;
    private GroupDao groupDao;

    public StudentServiceImpl() {
    }

    public StudentServiceImpl(StudentDao studentDao, GroupDao groupDao) throws ServiceException {
        if (studentDao == null || groupDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }

    @Autowired
    @Qualifier("studentDao")
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    @Qualifier("groupDao")
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public void changeFirstName(Student student, String newName) throws ServiceException {
        if (student == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.changeFirstName(student.getId(), newName);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeLastName(Student student, String newName) throws ServiceException {
        if (student == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.changeLastName(student.getId(), newName);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeGroup(Student student, Group newGroup) throws ServiceException {
        if (student == null || newGroup == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.changeGroup(student.getId(), newGroup.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Group getGroup(Student student) throws ServiceException {
        if (student == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return groupDao.findById(student.getGroupId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Student save(Student item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = studentDao.save(item);
            Student savedStudent = new Student();
            savedStudent.setId(generatedId);
            savedStudent.setFirstName(item.getFirstName());
            savedStudent.setLastName(item.getLastName());
            savedStudent.setGroupId(item.getGroupId());
            return savedStudent;
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Student item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.deleteById(item.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Student findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return studentDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Student> getAllItems() throws ServiceException {
        try {
            return studentDao.findAllRecords();
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
