package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DaoProperties {

    private Properties properties;
    private FileInputStream file;

    public DaoProperties(FileInputStream file){
        this.file = file;
    }

    public void initialize() throws DaoException {
        properties = new Properties();
        try {
            properties.load(file);
        } catch (IOException e) {
            throw new DaoException("Cannot load properties file");
        }
    }

    public String getQuery(String key){
        return properties.getProperty(key);
    }
}
