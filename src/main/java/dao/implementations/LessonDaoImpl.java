package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Lesson;
import dao.interfaces.LessonDao;
import dao.mappers.LessonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Component("lessonDao")
public class LessonDaoImpl implements LessonDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private static final Logger logger = LoggerFactory.getLogger(LessonDaoImpl.class);

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public LessonDaoImpl() {
    }

    public LessonDaoImpl(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
        if (jdbc == null || queries == null) {
            throw new DaoException(NULL_ERROR);
        }
        this.jdbc = jdbc;
        this.queries = queries;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Autowired
    @Qualifier("lessonProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(Lesson lesson) throws DaoException {
        logger.debug("Saving to database, item = {}", lesson);
        if (lesson == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"lesson_id"});
            ps.setInt(1, lesson.getCourseId());
            ps.setInt(2, lesson.getProfessorId());
            ps.setInt(3, lesson.getGroupId());
            ps.setInt(4, lesson.getAuditoryId());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is {}", lesson, generatedId);
        return generatedId;
    }

    @Override
    public void deleteById(int id) throws DaoException {
        logger.debug("Deleting from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Lesson findById(int id) throws DaoException {
        logger.debug("Retrieving from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new LessonMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Lesson> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new LessonMapper());
    }

    @Override
    public void changeAuditory(int lessonId, int auditoryId) throws DaoException {
        logger.debug("Changing auditory, lesson id = {}, new auditory id = {}", lessonId, auditoryId);
        if (lessonId <= 0 || auditoryId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeAuditory"), auditoryId, lessonId);
    }

    @Override
    public void changeCourse(int lessonId, int courseId) throws DaoException {
        logger.debug("Changing course, lesson id = {}, new course id = {}", lessonId, courseId);
        if (lessonId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeCourse"), courseId, lessonId);
    }

    @Override
    public void changeGroup(int lessonId, int groupId) throws DaoException {
        logger.debug("Changing group, lesson id = {}, new group id = {}", lessonId, groupId);
        if (lessonId <= 0 || groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeGroup"), groupId, lessonId);
    }

    @Override
    public void changeProfessor(int lessonId, int professorId) throws DaoException {
        logger.debug("Changing professor, lesson id = {}, new professor id = {}", lessonId, professorId);
        if (lessonId <= 0 || professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeProfessor"), professorId, lessonId);
    }

    @Override
    public void changeTime(int lessonId, int timeId) throws DaoException {
        logger.debug("Changing time, lesson id = {}, new time id = {}", lessonId, timeId);
        if (lessonId <= 0 || timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeTime"), timeId, lessonId);
    }

    @Override
    public void assignLessonToTime(int lessonId, int timeId) throws DaoException {
        logger.debug("Assigning lesson to time, lesson id = {}, time id = {}", lessonId, timeId);
        if (lessonId <= 0 || timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignLessonToTime"), lessonId, timeId);
    }

    @Override
    public List<Lesson> findByTime(int timeId) throws DaoException {
        logger.debug("Retrieving items by time, time id = {}", timeId);
        if (timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByTime"), new Object[]{timeId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findByGroupAndDate(int groupId, Date date) throws DaoException {
        logger.debug("Retrieving items by group and date, group id = {}, date = {}", groupId, date);
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (date == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findByGroupAndDate"),
                new Object[]{groupId, date}, new LessonMapper());
    }

    @Override
    public List<Lesson> findByGroupAndMonth(int groupId, Date month) throws DaoException {
        logger.debug("Retrieving items by group and month, group id = {}, month = {}", groupId, month);
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (month == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findByGroupAndMonth"),
                new Object[]{groupId, month}, new LessonMapper());
    }

    @Override
    public List<Lesson> findByProfessorAndDate(int professorId, Date date) throws DaoException {
        logger.debug("Retrieving items by professor and date, professor id = {}, date = {}", professorId, date);
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (date == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findByProfessorAndDate"),
                new Object[]{professorId, date}, new LessonMapper());
    }

    @Override
    public List<Lesson> findByProfessorAndMonth(int professorId, Date month) throws DaoException {
        logger.debug("Retrieving items by professor and month, professor id = {}, month = {}", professorId, month);
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (month == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findByProfessorAndMonth"),
                new Object[]{professorId, month}, new LessonMapper());
    }


}
