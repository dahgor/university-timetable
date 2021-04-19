package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Professor;
import dao.mappers.ProfessorMapper;
import dao.interfaces.ProfessorDao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProfessorDaoImpl implements ProfessorDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public ProfessorDaoImpl() {
    }

    public ProfessorDaoImpl(JdbcTemplate jdbc, DaoProperties queries) {
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
    public void save(Professor professor) throws DaoException {
        if (professor == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"),
                professor.getFirstName(), professor.getLastName());
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
    public List<Professor> findAllRecords() {
        return jdbc.query(queries.getQuery("findAllRecords"), new ProfessorMapper());
    }

    @Override
    public List<Professor> findProfessorsForCourse(int courseId) throws DaoException {
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findProfessorsForCourse"), new Object[]{courseId},
                new ProfessorMapper());
    }

    @Override
    public void assignProfessorToCourse(int professorId, int courseId) throws DaoException {
        if (professorId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignProfessorToCourse"), professorId, courseId);
    }
}
