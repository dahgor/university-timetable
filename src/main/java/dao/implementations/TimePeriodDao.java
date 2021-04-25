package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.TimePeriod;
import dao.interfaces.Dao;
import dao.mappers.TimePeriodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component("timePeriodDao")
public class TimePeriodDao implements Dao<TimePeriod> {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id is passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public TimePeriodDao() {
    }

    public TimePeriodDao(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
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
    @Qualifier("timePeriodProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(TimePeriod timePeriod) throws DaoException {
        if (timePeriod == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"time_period_id"});
            ps.setTimestamp(1, timePeriod.getStartHour());
            ps.setTimestamp(2, timePeriod.getEndHour());
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
    public List<TimePeriod> findAllRecords() throws DaoException {
        return jdbc.query(queries.getQuery("findAllRecords"), new TimePeriodMapper());
    }
}
