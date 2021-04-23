package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Group;
import dao.interfaces.GroupDao;
import dao.mappers.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("groupDao")
public class GroupDaoImpl implements GroupDao {
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    @Qualifier("groupProperties")
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

    public void setQueries(DaoProperties queries) {
        this.queries = queries;
    }

    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void save(Group group) throws DaoException {
        if (group == null) {
            throw new DaoException(NULL_ERROR);
        }
        jdbc.update(queries.getQuery("save"), group.getName());
    }

    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("deleteById"), id);
    }

    @Override
    public Group findById(int id) throws DaoException {
        if (id <= 0) {
            throw new DaoException(ID_ERROR);
        }
        return jdbc.query(queries.getQuery("findById"), new Object[]{id}, new GroupMapper())
                .stream().findAny().orElseThrow(() -> new DaoException("Item not found"));
    }

    @Override
    public List<Group> findAllRecords() throws DaoException {
        return jdbc.query(queries.getQuery("findAllRecords"), new GroupMapper());
    }

    @Override
    public void assignGroupToCourse(int groupId, int courseId) throws DaoException {
        if (groupId <= 0 || courseId <= 0) {
            throw new DaoException(ID_ERROR);
        }
        jdbc.update(queries.getQuery("assignGroupToCourse"), groupId, courseId);
    }
}
