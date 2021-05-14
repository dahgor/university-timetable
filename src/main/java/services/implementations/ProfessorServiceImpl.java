package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Professor;
import dao.interfaces.CourseDao;
import dao.interfaces.ProfessorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.ProfessorService;

import java.util.List;

@Component("professorService")
public class ProfessorServiceImpl implements ProfessorService {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final String DAO_ERROR = "Dao error occurred";

    private static final Logger logger = LoggerFactory.getLogger(ProfessorServiceImpl.class);

    private ProfessorDao professorDao;
    private CourseDao courseDao;

    public ProfessorServiceImpl() {
    }

    public ProfessorServiceImpl(ProfessorDao professorDao, CourseDao courseDao) throws ServiceException {
        if (professorDao == null || courseDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.professorDao = professorDao;
        this.courseDao = courseDao;
    }

    @Autowired
    public void setProfessorDao(ProfessorDao professorDao) throws ServiceException {
        if (professorDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.professorDao = professorDao;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) throws ServiceException {
        if (courseDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.courseDao = courseDao;
    }

    @Override
    public Professor save(Professor item) throws ServiceException {
        logger.info("Saving item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = professorDao.save(item);
            logger.debug("Generated id for {} is {}", item, generatedId);
            return new Professor(generatedId, item.getFirstName(), item.getLastName());
        } catch (DaoException e) {
            logger.warn("Failed to save item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Professor item) throws ServiceException {
        logger.info("Deleting item: {}", item);
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.deleteById(item.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete item: {}", item, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Professor findById(int id) throws ServiceException {
        logger.info("Retrieving item by id, id = {}", id);
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return professorDao.findById(id);
        } catch (DaoException e) {
            logger.warn("Failed to retrieve item by id: {}", id, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Professor> getAllItems() throws ServiceException {
        logger.info("Retrieving all items");
        try {
            return professorDao.findAllRecords();
        } catch (DaoException e) {
            logger.warn("Failed to retrieve all items", e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeFirstName(Professor professor, String newName) throws ServiceException {
        logger.info("Changing professor's first name, professor = {}, new name = {}", professor, newName);
        if (professor == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.changeFirstName(professor.getId(), newName);
        } catch (DaoException e) {
            logger.warn("Failed to change first name, item = {}, new name = {}", professor, newName, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeLastName(Professor professor, String newName) throws ServiceException {
        logger.info("Changing professor's last name, professor = {}, new name = {}", professor, newName);
        if (professor == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.changeLastName(professor.getId(), newName);
        } catch (DaoException e) {
            logger.warn("Failed to change last name, item = {}, new name = {}", professor, newName, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void addCourse(Professor professor, Course course) throws ServiceException {
        logger.info("Assigning professor to course, professor = {}, course = {}", professor, course);
        if (professor == null || course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.assignProfessorToCourse(professor.getId(), course.getId());
        } catch (DaoException e) {
            logger.warn("Failed to assign professor to course, professor = {}, course = {}",
                    professor, course, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void deleteCourse(Professor professor, Course course) throws ServiceException {
        logger.info("Deleting professor from course, professor = {}, course = {}", professor, course);
        if (professor == null || course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.deleteProfessorFromCourse(professor.getId(), course.getId());
        } catch (DaoException e) {
            logger.warn("Failed to delete professor from course, professor = {}, course = {}",
                    professor, course, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Course> getCourseList(Professor professor) throws ServiceException {
        logger.info("Retrieving professor's courses, professor = {}", professor);
        if (professor == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return courseDao.findByProfessor(professor.getId());
        } catch (DaoException e) {
            logger.warn("Failed to retrieve professor's courses, professor = {}", professor, e);
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
