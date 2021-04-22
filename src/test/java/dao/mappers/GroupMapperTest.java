package dao.mappers;

import dao.entities.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupMapperTest {
    private static final String INIT_SCRIPT_FILE = "classpath:sqlScripts/CreateTablesWithoutRelations.sql";

    private JdbcTemplate jdbcTemplate;
    private final Group group = new Group(1, "ME-15");

    @BeforeEach
    void prepareJdbcAndDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(INIT_SCRIPT_FILE)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    void saveGroup(Group group) {
        jdbcTemplate.update("insert into groups(group_name) values(?)", group.getName());
    }

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        saveGroup(group);
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from groups");

        Group result = null;
        if (resultSet.next()) {
            result = new GroupMapper().mapRow(resultSet, 1);
        }

        assertEquals(group, result);

        resultSet.close();
        statement.close();
        connection.close();
    }


}