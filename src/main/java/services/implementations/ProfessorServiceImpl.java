package services.implementations;

import dao.DaoException;
import dao.entities.Course;
import dao.entities.Professor;
import dao.interfaces.CourseDao;
import dao.interfaces.ProfessorDao;
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
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = professorDao.save(item);
            return new Professor(generatedId, item.getFirstName(), item.getLastName());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Professor item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.deleteById(item.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Professor findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return professorDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Professor> getAllItems() throws ServiceException {
        try {
            return professorDao.findAllRecords();
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeFirstName(Professor professor, String newName) throws ServiceException {
        if (professor == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.changeFirstName(professor.getId(), newName);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeLastName(Professor professor, String newName) throws ServiceException {
        if (professor == null || newName == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.changeLastName(professor.getId(), newName);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void addCourse(Professor professor, Course course) throws ServiceException {
        if (professor == null || course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.assignProfessorToCourse(professor.getId(), course.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void deleteCourse(Professor professor, Course course) throws ServiceException {
        if (professor == null || course == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            professorDao.deleteProfessorFromCourse(professor.getId(), course.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void getCourseList(Professor professor) throws ServiceException {
        if (professor == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            courseDao.findByProfessor(professor.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
