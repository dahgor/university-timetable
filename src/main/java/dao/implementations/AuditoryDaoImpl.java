package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Auditory;
import dao.interfaces.AuditoryDao;
import dao.mappers.AuditoryMapper;
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

@Component("auditoryDao")
public class AuditoryDaoImpl implements AuditoryDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private static final Logger logger = LoggerFactory.getLogger(AuditoryDaoImpl.class);

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public AuditoryDaoImpl() {
    }

    public AuditoryDaoImpl(JdbcTemplate jdbc, DaoProperties queries) throws DaoException {
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
    @Qualifier("auditoryProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Override
    public int save(Auditory auditory) throws DaoException {
        logger.debug("Saving {} to database", auditory);
        if (auditory == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"auditory_id"});
            ps.setString(1, auditory.getLocation());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is #{}", auditory, generatedId);
        return generatedId;
    }

    @Override
    public void deleteById(int id) throws DaoException {
        logger.debug("Deleting item with id #{}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Auditory findById(int id) throws DaoException {
        logger.debug("Retrieving from database item with id #{}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id},
                new AuditoryMapper())
                .stream().findAny()
                .orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Auditory> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new AuditoryMapper());
    }

    @Override
    public void changeLocation(int auditoryId, String newLocation) throws DaoException {
        logger.debug("Changing location. Item id = {}, new location = {}", auditoryId, newLocation);
        if (auditoryId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newLocation == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeLocation"), newLocation, auditoryId);
    }
}
