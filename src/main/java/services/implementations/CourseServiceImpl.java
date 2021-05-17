package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Group;
import dao.entities.Professor;
import dao.interfaces.CourseDao;
import dao.interfaces.GroupDao;
import dao.interfaces.ProfessorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.CourseService;

import java.util.List;

@Component("courseService")
public class CourseServiceImpl implements CourseService {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final String DAO_ERROR = "Dao error occurred";

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private CourseDao courseDao;
    private ProfessorDao professorDao;
    private GroupDao groupDao;

    public CourseServiceImpl() {
    }

    public CourseServiceImpl(CourseDao courseDao, ProfessorDao professorDao, GroupDao groupDao) throws ServiceException {
        if (courseDao == null || professorDao == null || groupDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.courseDao = courseDao;
        this.professorDao = professorDao;
        this.groupDao = groupDao;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setProfessorDao(ProfessorDao professorDao) {
        this.professorDao = professorDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }


    @Override
    public Course save(Course item) throws ServiceException {
        logger.debug("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = courseDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            return new Course(generatedId, item.getName(), item.getDescription());
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Course item) throws ServiceException {
        logger.debug("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Course findById(int id) throws ServiceException {
        logger.debug("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return courseDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Course> getAllItems() throws ServiceException {
        logger.debug("Retrieving all items");
        try {
            return courseDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items", e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeName(Course course, String newName) throws ServiceException {
        logger.debug("Changing course's name, course = {}, new name = {}", course, newName);
        if (course == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.changeName(course.getId(), newName);
        } catch (DaoException e) {
            logger.warn("Failed to change name, item = {}, new name = {}", course, newName, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeDescription(Course course, String newDescription) throws ServiceException {
        logger.debug("Changing course's description, course = {}, new description = {}", course, newDescription);
        if (course == null || newDescription == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.changeDescription(course.getId(), newDescription);
        } catch (DaoException e) {
            logger.warn("Failed to change description, item = {}, new description = {}", course, newDescription, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Professor> getProfessorList(Course course) throws ServiceException {
        logger.debug("Retrieving list of professors by course, course = {}", course);
        if (course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return professorDao.findByCourse(course.getId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve professors by course, course = {}", course, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Group> getGroupList(Course course) throws ServiceException {
        logger.debug("Retrieving list of groups by course, course = {}", course);
        if (course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return groupDao.findByCourse(course.getId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve groups by course, course = {}", course, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
