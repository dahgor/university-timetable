package dao.mappers;

import dao.entities.Professor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessorMapperTest {
    private static final int PROFESSOR_ID = 1;
    private static final String PROFESSOR_FIRST_NAME = "John";
    private static final String PROFESSOR_LAST_NAME = "Smith";

    @Mock
    private ResultSet resultSet;
    private ProfessorMapper professorMapper = new ProfessorMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("professor_id")).thenReturn(PROFESSOR_ID);
        when(resultSet.getString("first_name")).thenReturn(PROFESSOR_FIRST_NAME);
        when(resultSet.getString("last_name")).thenReturn(PROFESSOR_LAST_NAME);

        Professor result = professorMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("professor_id");
        verify(resultSet).getString("first_name");
        verify(resultSet).getString("last_name");
        assertEquals(PROFESSOR_ID, result.getId());
        assertEquals(PROFESSOR_FIRST_NAME, result.getFirstName());
        assertEquals(PROFESSOR_LAST_NAME, result.getLastName());
    }

}