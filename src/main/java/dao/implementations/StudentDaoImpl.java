package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Student;
import dao.interfaces.StudentDao;
import dao.mappers.StudentMapper;
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

@Component("studentDao")
public class StudentDaoImpl implements StudentDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id is passed";

    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    private JdbcTemplate jdbc;
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

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Autowired
    @Qualifier("studentProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(Student student) throws DaoException {
        logger.debug("Saving to database, item = {}", student);
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
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is {}", student, generatedId);
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
    public Student findById(int id) throws DaoException {
        logger.debug("Retrieving from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id}, new StudentMapper())
                .stream().findAny().orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Student> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new StudentMapper());
    }

    @Override
    public void changeGroup(int studentId, int groupId) throws DaoException {
        logger.debug("Changing group, item id = {}, new group id = {}", studentId, groupId);
        if (studentId <= 0 || groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("changeGroup"), groupId, studentId);
    }

    @Override
    public void changeFirstName(int studentId, String newFirstName) throws DaoException {
        logger.debug("Changing first name, item id = {}, new name = {}", studentId, newFirstName);
        if (studentId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newFirstName == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeFirstName"), newFirstName, studentId);
    }

    @Override
    public void changeLastName(int studentId, String newLastName) throws DaoException {
        logger.debug("Changing last name, item id = {}, new name = {}", studentId, newLastName);
        if (studentId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newLastName == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeLastName"), newLastName, studentId);
    }

    @Override
    public List<Student> findByGroup(int groupId) throws DaoException {
        logger.debug("Retrieving items by group, group id = {}", groupId);
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByGroup"), new Object[]{groupId},
                new StudentMapper());
    }

}
