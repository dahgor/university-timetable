package dao.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseMapperTest {
    @Mock
    private ResultSet resultSet;
    private CourseMapper courseMapper = new CourseMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        courseMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("course_id");
        verify(resultSet).getString("course_name");
        verify(resultSet).getString("course_description");
    }

}