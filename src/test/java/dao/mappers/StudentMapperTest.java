package dao.mappers;

import dao.entities.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentMapperTest {
    private static final int STUDENT_ID = 1;
    private static final int GROUP_ID = 2;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";

    @Mock
    private ResultSet resultSet;
    private StudentMapper studentMapper = new StudentMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("student_id")).thenReturn(STUDENT_ID);
        when(resultSet.getInt("group_id")).thenReturn(GROUP_ID);
        when(resultSet.getString("first_name")).thenReturn(FIRST_NAME);
        when(resultSet.getString("last_name")).thenReturn(LAST_NAME);

        Student result = studentMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("student_id");
        verify(resultSet).getInt("group_id");
        verify(resultSet).getString("first_name");
        verify(resultSet).getString("last_name");
        assertEquals(STUDENT_ID, result.getId());
        assertEquals(GROUP_ID, result.getGroupId());
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
    }
}