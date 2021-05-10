package dao.mappers;

import dao.entities.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupMapperTest {
    private static final int GROUP_ID = 1;
    private static final String GROUP_NAME = "ME-15";

    @Mock
    private ResultSet resultSet;
    private GroupMapper groupMapper = new GroupMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("group_id")).thenReturn(GROUP_ID);
        when(resultSet.getString("group_name")).thenReturn(GROUP_NAME);

        Group result = groupMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("group_id");
        verify(resultSet).getString("group_name");
        assertEquals(GROUP_ID, result.getId());
        assertEquals(GROUP_NAME, result.getName());
    }
}