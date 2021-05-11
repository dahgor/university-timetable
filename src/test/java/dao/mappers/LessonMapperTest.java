package dao.mappers;

import dao.entities.Lesson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonMapperTest {
    private static final int LESSON_ID = 1;
    private static final int AUDITORY_ID = 2;
    private static final int GROUP_ID = 3;
    private static final int COURSE_ID = 4;
    private static final int PROFESSOR_ID = 5;

    @Mock
    private ResultSet resultSet;
    private LessonMapper lessonMapper = new LessonMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("lesson_id")).thenReturn(LESSON_ID);
        when(resultSet.getInt("group_id")).thenReturn(GROUP_ID);
        when(resultSet.getInt("course_id")).thenReturn(COURSE_ID);
        when(resultSet.getInt("auditory_id")).thenReturn(AUDITORY_ID);
        when(resultSet.getInt("professor_id")).thenReturn(PROFESSOR_ID);

        Lesson result = lessonMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("lesson_id");
        verify(resultSet).getInt("group_id");
        verify(resultSet).getInt("course_id");
        verify(resultSet).getInt("auditory_id");
        verify(resultSet).getInt("professor_id");
        assertEquals(LESSON_ID, result.getId());
        assertEquals(AUDITORY_ID, result.getAuditoryId());
        assertEquals(GROUP_ID, result.getGroupId());
        assertEquals(COURSE_ID, result.getCourseId());
        assertEquals(PROFESSOR_ID, result.getProfessorId());
    }

}