package services.implementations;

import dao.DaoException;
import dao.entities.Group;
import dao.entities.Student;
import dao.interfaces.GroupDao;
import dao.interfaces.StudentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.StudentService;

import java.util.List;

@Component("studentService")
public class StudentServiceImpl implements StudentService {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final String DAO_ERROR = "Dao error occurred";

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

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
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public void changeFirstName(Student student, String newName) throws ServiceException {
        logger.info("Changing student's first name, student = {}, new name = {}", student, newName);
        if (student == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.changeFirstName(student.getId(), newName);
        } catch (DaoException e) {
            logger.warn("Failed to change first name, item = {}, new name = {}", student, newName, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeLastName(Student student, String newName) throws ServiceException {
        logger.info("Changing student's last name, student = {}, new name = {}", student, newName);
        if (student == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.changeLastName(student.getId(), newName);
        } catch (DaoException e) {
            logger.warn("Failed to change last name, item = {}, new name = {}", student, newName, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeGroup(Student student, Group newGroup) throws ServiceException {
        logger.info("Changing student's group, student = {}, new group = {}", student, groupDao);
        if (student == null || newGroup == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.changeGroup(student.getId(), newGroup.getId());
        } catch (DaoException e) {
            logger.warn("Failed to change student's group, student = {}, new group = {}",
                    student, newGroup, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Group getGroup(Student student) throws ServiceException {
        logger.info("Retrieving student's group, student = {}", student);
        if (student == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return groupDao.findById(student.getGroupId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve student's group, student = {}", student, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Student save(Student item) throws ServiceException {
        logger.info("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = studentDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            Student savedStudent = new Student();
            savedStudent.setId(generatedId);
            savedStudent.setFirstName(item.getFirstName());
            savedStudent.setLastName(item.getLastName());
            savedStudent.setGroupId(item.getGroupId());
            return savedStudent;
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Student item) throws ServiceException {
        logger.info("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            studentDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Student findById(int id) throws ServiceException {
        logger.info("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return studentDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Student> getAllItems() throws ServiceException {
        logger.info("Retrieving all items");
        try {
            return studentDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items", e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
