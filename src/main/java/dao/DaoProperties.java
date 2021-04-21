package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DaoProperties {
    public static final String NULL_ERROR = "Null is passed";
    public static final String LOAD_ERROR = "Cannot load properties file";

    private Properties properties;
    private FileInputStream file;

    public DaoProperties(FileInputStream file) throws DaoException {
        if (file == null) {
            throw new DaoException(NULL_ERROR);
        }
        this.file = file;
    }

    public String getQuery(String key) throws DaoException {
        if (key == null) {
            throw new DaoException(NULL_ERROR);
        }
        if (properties == null) {
            initialize();
        }
        return properties.getProperty(key);
    }

    private void initialize() throws DaoException {
        properties = new Properties();
        try {
            properties.load(file);
        } catch (IOException e) {
            throw new DaoException(LOAD_ERROR);
        }
    }
}
