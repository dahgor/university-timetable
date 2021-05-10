package dao.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditoryMapperTest {
    @Mock
    private ResultSet resultSet;
    private AuditoryMapper auditoryMapper = new AuditoryMapper();

    @Test
    void shouldCallCorrectMethodsOfResultSet() throws Exception {
        auditoryMapper.mapRow(resultSet, 1);

        verify(resultSet).getInt("auditory_id");
        verify(resultSet).getString("auditory_location");
    }
}