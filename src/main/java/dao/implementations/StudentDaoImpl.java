package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Student;
import dao.interfaces.StudentDao;
import dao.mappers.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component("studentDao")
public class StudentDaoImpl implements StudentDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id is passed";

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    @Qualifier("studentProperties")
    private DaoProperties queries;

    public StudentDaoImpl() {
    }

    public StudentDaoImpl(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
        if (jdbc == null || queries == null) {
            throw new DaoException(NULL_ERROR);
        }
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
    public int save(Student student) throws DaoException {
        if (student == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"student_id"});
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setInt(3, student.getGroupId());
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
    public Student findById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id}, new StudentMapper())
                .stream().findAny().orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Student> findAllRecords() throws DaoException {
        return jdbc.query(queries.getQuery("findAllRecords"), new StudentMapper());
    }

    @Override
    public List<Student> findStudentsInGroup(int groupId) throws DaoException {
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }

        return jdbc.query(queries.getQuery("findStudentsInGroup"), new Object[]{groupId},
                new StudentMapper());
    }

}
