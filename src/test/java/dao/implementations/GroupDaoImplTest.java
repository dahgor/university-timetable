package dao.implementations;

import dao.DaoException;
import dao.DaoProperties;
import dao.entities.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoImplTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    private static final String PROPERTIES = "./src/test/resources/daoProperties/groupDao.properties";
    private static final String NULL_ERROR = "Null is passed";
    private static final String ID_ERROR = "Invalid id passed";
    private static final int INVALID_ID = -1;

    private JdbcTemplate jdbcTemplate;
    private DaoProperties daoProperties;

    @BeforeEach
    void prepareJdbcAndProperties() throws FileNotFoundException, DaoException {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        FileInputStream file = new FileInputStream(PROPERTIES);
        daoProperties = new DaoProperties(file);
    }

    void saveGroup(Group group) {
        jdbcTemplate.update("insert into groups(group_name) values (?)",
                group.getName());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new GroupDaoImpl(null,
                null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToSaveMethod() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> groupDao.save(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToDeleteByIdMethod() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> groupDao.deleteById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenInvalidIdIsPassedToFindByIdMethod() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);

        Exception exception = assertThrows(DaoException.class, () -> groupDao.findById(INVALID_ID));
        assertEquals(ID_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnSameGroupFromDbWhenSaved() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group = new Group(1, "ME-15");

        groupDao.save(group);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from groups");

        assertTrue(result.next());
        assertEquals(group.getId(), result.getInt("group_id"));
        assertEquals(group.getName(), result.getString("group_name"));
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group = new Group(1, "ME-15");
        saveGroup(group);

        groupDao.deleteById(1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from groups");

        assertFalse(result.next());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group1 = new Group(1, "ME-15");
        Group group2 = new Group(2, "ME-16");
        saveGroup(group1);
        saveGroup(group2);

        List<Group> itemsFromDb = groupDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(group1, itemsFromDb.get(0));
        assertEquals(group2, itemsFromDb.get(1));
    }

    @Test
    void shouldAssignGroupToCourse() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group = new Group(1, "ME-15");
        saveGroup(group);
        jdbcTemplate.execute("insert into courses(course_name, course_description) VALUES ('Math', 'description')");

        groupDao.assignGroupToCourse(1, 1);
        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from group_course");

        assertTrue(result.next());
        assertEquals(1, result.getInt("group_id"));
        assertEquals(1, result.getInt("course_id"));
    }

}