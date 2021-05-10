package dao.mappers;

import dao.entities.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseMapperTest {
    private static final int COURSE_ID = 1;
    private static final String COURSE_NAME = "Math";
    private static final String COURSE_DESCRIPTION = "Description";

    @Mock
    private ResultSet resultSet;
    private CourseMapper courseMapper = new CourseMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("course_id")).thenReturn(COURSE_ID);
        when(resultSet.getString("course_name")).thenReturn(COURSE_NAME);
        when(resultSet.getString("course_description")).thenReturn(COURSE_DESCRIPTION);

        Course result = courseMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("course_id");
        verify(resultSet).getString("course_name");
        verify(resultSet).getString("course_description");
        assertEquals(COURSE_ID, result.getId());
        assertEquals(COURSE_NAME, result.getName());
        assertEquals(COURSE_DESCRIPTION, result.getDescription());
    }

}