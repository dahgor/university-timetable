package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Course;
import dao.interfaces.CourseDao;
import dao.mappers.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component("courseDao")
public class CourseDaoImpl implements CourseDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public CourseDaoImpl() {
    }

    public CourseDaoImpl(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
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
    @Qualifier("courseProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(Course course) throws DaoException {
        logger.debug("Saving to database, item = {}", course);
        if (course == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"course_id"});
            ps.setString(1, course.getName());
            ps.setString(2, course.getDescription());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is {}", course, generatedId);
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
    public Course findById(int id) throws DaoException {
        logger.debug("Retrieving from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new CourseMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Course> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new CourseMapper());
    }

    @Override
    public void changeName(int courseId, String newName) throws DaoException {
        logger.debug("Changing name, item id = {}, new name = {}", courseId, newName);
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newName == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeName"), newName, courseId);
    }

    @Override
    public void changeDescription(int courseId, String newDescription) throws DaoException {
        logger.debug("Changing description, item id = {}, new description = {}", courseId, newDescription);
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newDescription == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeDescription"), newDescription, courseId);
    }

    @Override
    public List<Course> findByProfessor(int professorId) throws DaoException {
        logger.debug("Retrieving items by professor, professor id = {}", professorId);
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByProfessor"), new Object[]{professorId},
                new CourseMapper());
    }

    @Override
    public List<Course> findByGroup(int groupId) throws DaoException {
        logger.debug("Retrieving items by group, group id = {}", groupId);
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByGroup"), new Object[]{groupId},
                new CourseMapper());
    }
}
