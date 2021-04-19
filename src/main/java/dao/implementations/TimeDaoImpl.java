package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Time;
import dao.mappers.TimeMapper;
import dao.interfaces.TimeDao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TimeDaoImpl implements TimeDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public TimeDaoImpl() {
    }

    public TimeDaoImpl(JdbcTemplate jdbc, DaoProperties queries) {
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
    public void save(Time time) throws DaoException {
        if (time == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"), time.getDate(), time.getTimePeriodId());
    }

    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Time findById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id}, new TimeMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Time> findAllRecords() throws DaoException {
        return jdbc.query(queries.getQuery("findAllRecords"), new TimeMapper());
    }

    @Override
    public Time findScheduledTimeForLesson(int lessonId) throws DaoException {
        if (lessonId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findScheduledTimeForLesson"),
                new Object[]{lessonId}, new TimeMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

}
