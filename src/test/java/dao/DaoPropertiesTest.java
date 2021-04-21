package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DaoPropertiesTest {
    public static final String NULL_ERROR = "Null is passed";
    public static final String LOAD_ERROR = "Cannot load properties file";
    public static final String FILE = "./src/test/resources/testDaoProperties.properties";
    public static final String PROPERTY_KEY = "property";
    public static final String PROPERTY_VALUE = "this is property";

    private FileInputStream file;

    @BeforeEach
    void prepareFiles() throws FileNotFoundException {
        file = new FileInputStream(FILE);
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToConstructor() {
        Exception exception = assertThrows(DaoException.class, () -> new DaoProperties(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenNullIsPassedToGetQueryMethod() {
        Exception exception = assertThrows(DaoException.class,
                () -> new DaoProperties(file).getQuery(null));
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowDaoExceptionWhenTryingToWorkWithBadFile() throws DaoException, IOException {
        DaoProperties daoProperties = new DaoProperties(file);
        file.close();

        Exception exception = assertThrows(DaoException.class,
                () -> daoProperties.getQuery(PROPERTY_KEY));

        assertEquals(LOAD_ERROR, exception.getMessage());
    }

    @Test
    void shouldReturnCorrectPropertyValueWhenValidKeyIsPassed() throws DaoException {
        DaoProperties daoProperties = new DaoProperties(file);

        String result = daoProperties.getQuery(PROPERTY_KEY);

        assertEquals(PROPERTY_VALUE, result);
    }

}