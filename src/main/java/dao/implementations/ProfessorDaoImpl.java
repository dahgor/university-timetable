package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Professor;
import dao.interfaces.ProfessorDao;
import dao.mappers.ProfessorMapper;
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

@Component("professorDao")
public class ProfessorDaoImpl implements ProfessorDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private static final Logger logger = LoggerFactory.getLogger(ProfessorDaoImpl.class);

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public ProfessorDaoImpl() {
    }

    public ProfessorDaoImpl(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
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
    @Qualifier("professorProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(Professor professor) throws DaoException {
        logger.debug("Saving to database, item = {}", professor);
        if (professor == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"professor_id"});
            ps.setString(1, professor.getFirstName());
            ps.setString(2, professor.getLastName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is {}", professor, generatedId);
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
    public Professor findById(int id) throws DaoException {
        logger.debug("Retrieving from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new ProfessorMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Professor> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new ProfessorMapper());
    }

    @Override
    public List<Professor> findByCourse(int courseId) throws DaoException {
        logger.debug("Retrieving items by course, course id = {}", courseId);
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByCourse"), new Object[]{courseId},
                new ProfessorMapper());
    }

    @Override
    public void changeFirstName(int professorId, String newFirstName) throws DaoException {
        logger.debug("Changing first name, item id = {}, new name = {}", professorId, newFirstName);
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newFirstName == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeFirstName"), newFirstName, professorId);
    }

    @Override
    public void changeLastName(int professorId, String newLastName) throws DaoException {
        logger.debug("Changing last name, item id = {}, new name = {}", professorId, newLastName);
        if (professorId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newLastName == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeLastName"), newLastName, professorId);
    }

    @Override
    public void assignProfessorToCourse(int professorId, int courseId) throws DaoException {
        logger.debug("Assigning professor to course, professor id = {}, course id = {}", professorId, courseId);
        if (professorId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignProfessorToCourse"), professorId, courseId);
    }

    @Override
    public void deleteProfessorFromCourse(int professorId, int courseId) throws DaoException {
        logger.debug("Deleting professor from course, professor id = {}, course id = {}", professorId, courseId);
        if (professorId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteProfessorFromCourse"), professorId, courseId);
    }
}
