package configuration;

import dao.DaoException;
import dao.DaoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class DaoPropertiesConfig {

    @Bean
    public DaoProperties groupProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/groupDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties studentProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/studentDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties courseProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/courseDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties professorProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/professorDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties auditoryProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/auditoryDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties lessonProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/lessonDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties timePeriodProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/timePeriodDao.properties").getFile());
        return new DaoProperties(file);
    }

    @Bean
    public DaoProperties timeProperties() throws IOException, DaoException {
        FileInputStream file =
                new FileInputStream(new ClassPathResource("daoProperties/timeDao.properties").getFile());
        return new DaoProperties(file);
    }
}
