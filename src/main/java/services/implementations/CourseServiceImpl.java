package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Group;
import dao.entities.Professor;
import dao.interfaces.CourseDao;
import dao.interfaces.GroupDao;
import dao.interfaces.ProfessorDao;
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
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = courseDao.save(item);
            return new Course(generatedId, item.getName(), item.getDescription());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Course item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.deleteById(item.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Course findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return courseDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Course> getAllItems() throws ServiceException {
        try {
            return courseDao.findAllRecords();
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeName(Course course, String newName) throws ServiceException {
        if (course == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.changeName(course.getId(), newName);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeDescription(Course course, String newDescription) throws ServiceException {
        if (course == null || newDescription == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.changeDescription(course.getId(), newDescription);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Professor> getProfessorList(Course course) throws ServiceException {
        if (course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return professorDao.findByCourse(course.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Group> getGroupList(Course course) throws ServiceException {
        if (course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return groupDao.findByCourse(course.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
