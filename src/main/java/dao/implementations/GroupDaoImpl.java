package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Group;
import dao.interfaces.GroupDao;
import dao.mappers.GroupMapper;
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

@Component("groupDao")
public class GroupDaoImpl implements GroupDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    private static final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);

    private JdbcTemplate jdbc;
    private DaoProperties queries;

    public GroupDaoImpl() {
    }

    public GroupDaoImpl(JdbcTemplate jdbcTemplate, DaoProperties properties) throws DaoException {
        if (jdbcTemplate == null || properties == null) {
            throw new DaoException(NULL_ERROR);
        }
        this.jdbc = jdbcTemplate;
        this.queries = properties;
    }

    @Autowired
    @Qualifier("groupProperties")
    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int save(Group group) throws DaoException {
        logger.debug("Saving to database, item = {}", group);
        if (group == null) {
            throw new DaoException(NULL_ERROR);
        }
        final String query = queries.getQuery("save");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"group_id"});
            ps.setString(1, group.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Failed to get generated id from database");
        }
        int generatedId = keyHolder.getKey().intValue();
        logger.debug("Generated id for {} is {}", group, generatedId);
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
    public Group findById(int id) throws DaoException {
        logger.debug("Retrieving from database, item id = {}", id);
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id}, new GroupMapper())
                .stream().findAny().orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Group> findAllRecords() throws DaoException {
        logger.debug("Retrieving all items from database");
        return jdbc.query(queries.getQuery("findAllRecords"), new GroupMapper());
    }

    @Override
    public void changeName(int groupId, String newName) throws DaoException {
        logger.debug("Changing name, item id = {}, new name = {}", groupId, newName);
        if (groupId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        if (newName == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("changeName"), newName, groupId);
    }

    @Override
    public void assignGroupToCourse(int groupId, int courseId) throws DaoException {
        logger.debug("Assigning group to course, group id = {}, course id = {}", groupId, courseId);
        if (groupId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignGroupToCourse"), groupId, courseId);
    }

    @Override
    public void deleteGroupFromCourse(int groupId, int courseId) throws DaoException {
        logger.debug("Deleting group from course, group id = {}, course id = {}", groupId, courseId);
        if (groupId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteGroupFromCourse"), groupId, courseId);
    }

    @Override
    public List<Group> findByCourse(int courseId) throws DaoException {
        logger.debug("Retrieving items by course, course id = {}", courseId);
        if (courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findByCourse"), new Object[]{courseId}, new GroupMapper());
    }
}
