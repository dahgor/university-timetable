package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.TimePeriod;
import dao.mappers.TimePeriodMapper;
import dao.interfaces.Dao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TimePeriodDao implements Dao<TimePeriod> {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id is passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public TimePeriodDao() {
    }

    public TimePeriodDao(JdbcTemplate jdbc, DaoProperties queries) {
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
    public void save(TimePeriod timePeriod) throws DaoException {
        if (timePeriod == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"), timePeriod.getStartHour(),
                timePeriod.getEndHour());
    }

    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public TimePeriod findById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new TimePeriodMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<TimePeriod> findAllRecords() {
        return jdbc.query(queries.getQuery("findAllRecords"), new TimePeriodMapper());
    }
}
