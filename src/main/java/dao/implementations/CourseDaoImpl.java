package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Course;
import dao.mappers.CourseMapper;
import dao.interfaces.CourseDao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CourseDaoImpl implements CourseDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public CourseDaoImpl() {
    }

    public CourseDaoImpl(JdbcTemplate jdbc, DaoProperties queries) {
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
    public void save(Course course) throws DaoException {
        if (course == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"), course.getName(), course.getDescription());
    }

    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Course findById(int id) throws DaoException {
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
        return jdbc.query(queries.getQuery("findAllRecords"), new CourseMapper());
    }

    @Override
    public List<Course> findCoursesForProfessor(int professorId) throws DaoException {
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findCoursesForProfessor"), new Object[]{professorId},
                new CourseMapper());
    }

    @Override
    public List<Course> findCoursesForGroup(int groupId) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findCoursesForGroup"), new Object[]{groupId},
                new CourseMapper());
    }
}
