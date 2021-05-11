package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Professor;
import dao.interfaces.ProfessorDao;
import dao.mappers.ProfessorMapper;
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
    public Professor findById(int id) throws DaoException {
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
        return jdbc.query(queries.getQuery("findAllRecords"), new ProfessorMapper());
    }

    @Override
    public List<Professor> findByCourse(int courseId) throws DaoException {
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByCourse"), new Object[]{courseId},
                new ProfessorMapper());
    }

    @Override
    public void changeFirstName(int professorId, String newFirstName) throws DaoException {
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
        if (professorId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignProfessorToCourse"), professorId, courseId);
    }

    @Override
    public void deleteProfessorFromCourse(int professorId, int courseId) throws DaoException {
        if (professorId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteProfessorFromCourse"), professorId, courseId);
    }
}