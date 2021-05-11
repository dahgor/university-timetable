package configuration;

import dao.DaoException;
import dao.DaoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Configuration
public class DaoPropertiesConfig {

    @Bean
    public DaoProperties groupProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/groupDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties studentProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/studentDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties courseProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/courseDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties professorProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/professorDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties auditoryProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/auditoryDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties lessonProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/lessonDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties timePeriodProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/timePeriodDao.properties");
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties timeProperties() throws FileNotFoundException, DaoException {
        FileInputStream file =
                new FileInputStream("./src/main/resources/daoProperties/timeDao.properties");
        return new DaoProperties(file);
    }
}
