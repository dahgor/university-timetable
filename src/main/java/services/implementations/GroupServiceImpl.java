package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Group;
import dao.entities.Student;
import dao.interfaces.CourseDao;
import dao.interfaces.GroupDao;
import dao.interfaces.StudentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.GroupService;

import java.util.List;

@Component
public class GroupServiceImpl implements GroupService {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final String DAO_ERROR = "Dao error occurred";

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private GroupDao groupDao;
    private StudentDao studentDao;
    private CourseDao courseDao;

    public GroupServiceImpl() {
    }

    public GroupServiceImpl(GroupDao groupDao, StudentDao studentDao, CourseDao courseDao) throws ServiceException {
        if (groupDao == null || studentDao == null || courseDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public void changeName(Group group, String newName) throws ServiceException {
        logger.debug("Changing group's name, group = {}, new name = {}", group, newName);
        if (group == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            groupDao.changeName(group.getId(), newName);
        } catch (DaoException e) {
            logger.warn("Failed to change name, item = {}, new name = {}", group, newName, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void addCourse(Group group, Course course) throws ServiceException {
        logger.debug("Assigning group to course, group = {}, course = {}", group, course);
        if (group == null || course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            groupDao.assignGroupToCourse(group.getId(), course.getId());
        } catch (DaoException e) {
            logger.warn("Failed to assign group to course, group = {}, course = {}", group, course, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void deleteCourse(Group group, Course course) throws ServiceException {
        logger.debug("Deleting group from course, group = {}, course = {}", group, course);
        if (group == null || course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            groupDao.deleteGroupFromCourse(group.getId(), course.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete group from course, group = {}, course = {}",
                    group, course, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Course> getCourseList(Group group) throws ServiceException {
        logger.debug("Retrieving list of courses by group, group = {}", group);
        if (group == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return courseDao.findByGroup(group.getId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve courses by group, group = {}", group, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Student> getStudentList(Group group) throws ServiceException {
        logger.debug("Retrieving list of students by group, group = {}", group);
        if (group == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return studentDao.findByGroup(group.getId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve students by group, group = {}", group, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Group save(Group item) throws ServiceException {
        logger.debug("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = groupDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            Group savedGroup = new Group();
            savedGroup.setId(generatedId);
            savedGroup.setName(item.getName());
            return savedGroup;
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Group item) throws ServiceException {
        logger.debug("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            groupDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Group findById(int id) throws ServiceException {
        logger.debug("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return groupDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Group> getAllItems() throws ServiceException {
        logger.debug("Retrieving all items");
        try {
            return groupDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items", e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
