package services.implementations;

import dao.DaoException;
import dao.entities.*;
import dao.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ServiceException;
import services.interfaces.LessonService;

import java.sql.Date;
import java.util.List;

@Component("lessonService")
public class LessonServiceImpl implements LessonService {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final String DAO_ERROR = "Dao error occurred";

    private LessonDao lessonDao;
    private GroupDao groupDao;
    private CourseDao courseDao;
    private ProfessorDao professorDao;
    private AuditoryDao auditoryDao;

    public LessonServiceImpl() {
    }

    public LessonServiceImpl(LessonDao lessonDao, GroupDao groupDao, CourseDao courseDao,
                             ProfessorDao professorDao, AuditoryDao auditoryDao) throws ServiceException {
        if (lessonDao == null || groupDao == null || courseDao == null ||
                professorDao == null || auditoryDao == null) {
            throw new ServiceException(NULL_ERROR);
        }
        this.lessonDao = lessonDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.professorDao = professorDao;
        this.auditoryDao = auditoryDao;
    }

    @Autowired
    public void setLessonDao(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
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
    public void setAuditoryDao(AuditoryDao auditoryDao) {
        this.auditoryDao = auditoryDao;
    }

    @Override
    public Lesson save(Lesson item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            int generatedId = lessonDao.save(item);
            Lesson savedLesson = new Lesson();
            savedLesson.setId(generatedId);
            savedLesson.setAuditoryId(item.getAuditoryId());
            savedLesson.setCourseId(item.getCourseId());
            savedLesson.setGroupId(item.getGroupId());
            savedLesson.setProfessorId(item.getProfessorId());
            return savedLesson;
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void delete(Lesson item) throws ServiceException {
        if (item == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.deleteById(item.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Lesson findById(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException(ID_ERROR);
        }
        try {
            return lessonDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Lesson> getAllItems() throws ServiceException {
        try {
            return lessonDao.findAllRecords();
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeAuditory(Lesson lesson, Auditory newAuditory) throws ServiceException {
        if (lesson == null || newAuditory == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.changeAuditory(lesson.getId(), newAuditory.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeGroup(Lesson lesson, Group newGroup) throws ServiceException {
        if (lesson == null || newGroup == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.changeGroup(lesson.getId(), newGroup.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeProfessor(Lesson lesson, Professor newProfessor) throws ServiceException {
        if (lesson == null || newProfessor == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.changeProfessor(lesson.getId(), newProfessor.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeCourse(Lesson lesson, Course newCourse) throws ServiceException {
        if (lesson == null || newCourse == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.changeCourse(lesson.getId(), newCourse.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void changeTime(Lesson lesson, Time newTime) throws ServiceException {
        if (lesson == null || newTime == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.changeTime(lesson.getId(), newTime.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public void assignTime(Lesson lesson, Time time) throws ServiceException {
        if (lesson == null || time == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            lessonDao.assignLessonToTime(lesson.getId(), time.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Auditory getAuditory(Lesson lesson) throws ServiceException {
        if (lesson == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return auditoryDao.findById(lesson.getAuditoryId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Group getGroup(Lesson lesson) throws ServiceException {
        if (lesson == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return groupDao.findById(lesson.getGroupId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Course getCourse(Lesson lesson) throws ServiceException {
        if (lesson == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return courseDao.findById(lesson.getCourseId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public Professor getProfessor(Lesson lesson) throws ServiceException {
        if (lesson == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return professorDao.findById(lesson.getProfessorId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Lesson> getAllForTime(Time time) throws ServiceException {
        if (time == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return lessonDao.findByTime(time.getId());
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Lesson> getAllForGroupAndDate(Group group, Date date) throws ServiceException {
        if (group == null || date == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return lessonDao.findByGroupAndDate(group.getId(), date);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Lesson> getAllForGroupAndMonth(Group group, Date month) throws ServiceException {
        if (group == null || month == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return lessonDao.findByGroupAndMonth(group.getId(), month);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Lesson> getAllForProfessorAndDate(Professor professor, Date date) throws ServiceException {
        if (professor == null || date == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return lessonDao.findByProfessorAndDate(professor.getId(), date);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }

    @Override
    public List<Lesson> getAllForProfessorAndMonth(Professor professor, Date month) throws ServiceException {
        if (professor == null || month == null) {
            throw new ServiceException(NULL_ERROR);
        }
        try {
            return lessonDao.findByProfessorAndMonth(professor.getId(), month);
        } catch (DaoException e) {
            throw new ServiceException(DAO_ERROR, e);
        }
    }
}
