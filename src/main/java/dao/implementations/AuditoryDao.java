package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Auditory;
import dao.mappers.AuditoryMapper;
import dao.interfaces.Dao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AuditoryDao implements Dao<Auditory> {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public AuditoryDao() {
    }

    public AuditoryDao(JdbcTemplate jdbc, DaoProperties queries) {
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
    public void save(Auditory auditory) throws DaoException {
        if (auditory == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"), auditory.getLocation());
    }

    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Auditory findById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new AuditoryMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Auditory> findAllRecords() {
        return jdbc.query(queries.getQuery("findAllRecords"), new AuditoryMapper());
    }
}
