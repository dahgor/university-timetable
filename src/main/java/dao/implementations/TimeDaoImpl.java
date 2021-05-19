package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Time;
import dao.interfaces.TimeDao;
import dao.mappers.TimeMapper;
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

@Component("timeDao")
public class TimeDaoImpl implements TimeDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private static final Logger logger = LoggerFactory.getLogger(TimeDaoImpl.class);

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public TimeDaoImpl() {
    }

    public TimeDaoImpl(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
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
    @Qualifier("timeProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(Time time) throws DaoException {
        logger.debug("Saving to database, item = {}", time);
        if (time == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"time_id"});
            ps.setDate(1, time.getDate());
            ps.setInt(2, time.getTimePeriodId());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is {}", time, generatedId);
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
    public Time findById(int id) throws DaoException {
        logger.debug("Retrieving from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id}, new TimeMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Time> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new TimeMapper());
    }

    @Override
    public Time findScheduledTimeForLesson(int lessonId) throws DaoException {
        logger.debug("Retrieving item by lesson, lesson id = {}", lessonId);
        if (lessonId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findScheduledTimeForLesson"),
                new Object[]{lessonId}, new TimeMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

}
