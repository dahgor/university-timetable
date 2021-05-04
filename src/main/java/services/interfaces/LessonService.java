package services.interfaces;

import dao.entities.*;
import services.ServiceException;

import java.sql.Date;
import java.util.List;

public interface LessonService extends Service<Lesson> {

    void changeAuditory(Lesson lesson, Auditory newAuditory) throws ServiceException;

    void changeGroup(Lesson lesson, Group newGroup) throws ServiceException;

    void changeProfessor(Lesson lesson, Professor newProfessor) throws ServiceException;

    void changeCourse(Lesson lesson, Course newCourse) throws ServiceException;

    void changeTime(Lesson lesson, Time newTime) throws ServiceException;

    void assignTime(Lesson lesson, Time time) throws ServiceException;

    List<Lesson> getAllForTime(Time time) throws ServiceException;

    List<Lesson> getAllForGroupAndDate(Group group, Date date) throws ServiceException;

    List<Lesson> getAllForGroupAndMonth(Group group, Date month) throws ServiceException;

    List<Lesson> getAllForProfessorAndDate(Professor professor, Date date) throws ServiceException;

    List<Lesson> getAllForProfessorAndMonth(Professor professor, Date month) throws ServiceException;
}
