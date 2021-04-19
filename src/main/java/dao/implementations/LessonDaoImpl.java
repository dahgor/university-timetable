package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Lesson;
import dao.mappers.LessonMapper;
import dao.interfaces.LessonDao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class LessonDaoImpl implements LessonDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public LessonDaoImpl() {
    }

    public LessonDaoImpl(JdbcTemplate jdbc, DaoProperties queries) {
        this.jdbc = jdbc;
        this.queries = queries;
    }

    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public void save(Lesson lesson) throws DaoException {
        if (lesson == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"), lesson.getCourseId(),
                lesson.getProfessorId(), lesson.getGroupId(),
                lesson.getAuditoryId());
    }

    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Lesson findById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new LessonMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Lesson> findAllRecords() {
        return jdbc.query(queries.getQuery("findAllRecords"), new LessonMapper());
    }

    @Override
    public List<Lesson> findScheduledLessonsForTime(int timeId) throws DaoException {
        if (timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findScheduledLessonsForTime"), new Object[]{timeId},
                new LessonMapper());
    }

    public void assignLessonToTime(int lessonId, int timeId) throws DaoException {
        if (lessonId <= 0 || timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignLessonToTime"), lessonId, timeId);
    }
}
