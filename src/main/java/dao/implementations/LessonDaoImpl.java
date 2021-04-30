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
    public void changeAuditory(int lessonId, int auditoryId) throws DaoException {
        if (lessonId <= 0 || auditoryId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeAuditory"), auditoryId, lessonId);
    }

    @Override
    public void changeCourse(int lessonId, int courseId) throws DaoException {
        if (lessonId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeCourse"), courseId, lessonId);
    }

    @Override
    public void changeGroup(int lessonId, int groupId) throws DaoException {
        if (lessonId <= 0 || groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeGroup"), groupId, lessonId);
    }

    @Override
    public void changeProfessor(int lessonId, int professorId) throws DaoException {
        if (lessonId <= 0 || professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeProfessor"), professorId, lessonId);
    }

    @Override
    public void changeTime(int lessonId, int timeId) throws DaoException {
        if (lessonId <= 0 || timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeTime"), timeId, lessonId);
    }

    @Override
    public void assignLessonToTime(int lessonId, int timeId) throws DaoException {
        if (lessonId <= 0 || timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignLessonToTime"), lessonId, timeId);
    }

    @Override
    public List<Lesson> findByTime(int timeId) throws DaoException {
        if (timeId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByTime"), new Object[]{timeId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findByCourse(int courseId) throws DaoException {
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByCourse"), new Object[]{courseId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findByProfessor(int professorId) throws DaoException {
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByProfessor"), new Object[]{professorId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findByGroup(int groupId) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByGroup"), new Object[]{groupId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findByAuditory(int auditoryId) throws DaoException {
        if (auditoryId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByAuditory"), new Object[]{auditoryId},
                new LessonMapper());
    }

    @Override
    public List<Lesson> findByGroupAndDate(int groupId, Date day) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (day == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findByGroupAndDate"),
                new Object[]{groupId, day}, new LessonMapper());
    }

    @Override
    public List<Lesson> findByGroupAndMonth(int groupId, Date month) throws DaoException {
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
    public List<Lesson> findByProfessorAndDate(int professorId, Date day) throws DaoException {
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (day == null) {
            throw new DaoException(NULL_ERROR);
        }
        return jdbc.query(queries.getQuery("findByProfessorAndDate"),
                new Object[]{professorId, day}, new LessonMapper());
    }

    @Override
    public List<Lesson> findByProfessorAndMonth(int professorId, Date month) throws DaoException {
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
