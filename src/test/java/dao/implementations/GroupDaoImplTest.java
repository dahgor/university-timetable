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
    public static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTables.sql";
    public static final String PROPERTIES = "./src/test/resources/daoProperties/groupDao.properties";
    public static final String NULL_ERROR = "Null is passed";
    public static final String ID_ERROR = "Invalid id passed";
    public static final int INVALID_ID = -1;

    private JdbcTemplate jdbcTemplate;
    private DaoProperties daoProperties;

    @BeforeEach
    void prepareJdbcAndProperties() throws FileNotFoundException {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        FileInputStream file = new FileInputStream(PROPERTIES);
        daoProperties = new DaoProperties(file);
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
        Group result = groupDao.findById(1);

        assertEquals(group, result);
    }

    @Test
    void shouldDeleteItemFromDbWhenValidIdIsPassed() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group = new Group(1, "ME-15");
        groupDao.save(group);
        List<Group> itemsFromDb = groupDao.findAllRecords();
        assertEquals(1, itemsFromDb.size());

        groupDao.deleteById(1);
        itemsFromDb = groupDao.findAllRecords();

        assertEquals(0, itemsFromDb.size());
    }

    @Test
    void shouldRetrieveItemsFromDb() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group1 = new Group(1, "ME-15");
        Group group2 = new Group(2, "ME-16");
        groupDao.save(group1);
        groupDao.save(group2);

        List<Group> itemsFromDb = groupDao.findAllRecords();

        assertEquals(2, itemsFromDb.size());
        assertEquals(group1, itemsFromDb.get(0));
        assertEquals(group2, itemsFromDb.get(1));
    }

    @Test
    void shouldAssignGroupToCourse() throws DaoException {
        GroupDaoImpl groupDao = new GroupDaoImpl(jdbcTemplate, daoProperties);
        Group group = new Group(1, "ME-15");
        groupDao.save(group);
        jdbcTemplate.execute("insert into courses(course_name, course_description) VALUES ('Math', 'description')");
        groupDao.assignGroupToCourse(1, 1);

        SqlRowSet result = jdbcTemplate.queryForRowSet("select * from group_course");

        assertTrue(result.next());
        assertEquals(1, result.getInt("group_id"));
        assertEquals(1, result.getInt("course_id"));
    }

}