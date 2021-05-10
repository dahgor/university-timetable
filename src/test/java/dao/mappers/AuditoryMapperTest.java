package dao.mappers;

import dao.entities.Auditory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditoryMapperTest {
    private static final int AUDITORY_ID = 1;
    private static final String AUDITORY_LOCATION = "1st floor";

    @Mock
    private ResultSet resultSet;
    private AuditoryMapper auditoryMapper = new AuditoryMapper();

    @Test
    void shouldTransformResultSetIntoEntityClassWhenDataIsProvided() throws Exception {
        when(resultSet.getInt("auditory_id")).thenReturn(AUDITORY_ID);
        when(resultSet.getString("auditory_location")).thenReturn(AUDITORY_LOCATION);

        Auditory result = auditoryMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("auditory_id");
        verify(resultSet).getString("auditory_location");
        assertEquals(AUDITORY_ID, result.getId());
        assertEquals(AUDITORY_LOCATION, result.getLocation());
    }
}