package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Lesson;
import dao.interfaces.LessonDao;
import dao.mappers.LessonMapper;
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

        return keyHolder.getKey().intValue();
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
    public List<Lesson> findAllRecords() throws DaoException {
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

    @Override
    public void assignLessonToTime(int lessonId, int timeId) throws DaoException {
        if (lessonId <= 0 || timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignLessonToTime"), lessonId, timeId);
    }

    @Override
    public List<Lesson> findLessonsWithCourse(int courseId) throws DaoException {
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsWithCourse"), new Object[]{courseId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsWithProfessor(int professorId) throws DaoException {
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsWithProfessor"), new Object[]{professorId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsWithGroup(int groupId) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsWithGroup"), new Object[]{groupId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsWithAuditory(int auditoryId) throws DaoException {
        if (auditoryId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsWithAuditory"), new Object[]{auditoryId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsForGroupForDay(int groupId, Date day) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (day == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsForGroupForDay"),
                new Object[]{groupId, day}, new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsForGroupForMonth(int groupId, Date month) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (month == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsForGroupForMonth"),
                new Object[]{groupId, month}, new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsForProfessorForDay(int professorId, Date day) throws DaoException {
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (day == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsForProfessorForDay"),
                new Object[]{professorId, day}, new LessonMapper());
    }

    @Override
    public List<Lesson> findLessonsForProfessorForMonth(int professorId, Date month) throws DaoException {
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (month == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findLessonsForProfessorForMonth"),
                new Object[]{professorId, month}, new LessonMapper());
    }


}
